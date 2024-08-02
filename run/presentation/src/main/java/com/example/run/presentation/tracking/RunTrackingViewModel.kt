package com.example.run.presentation.tracking

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.dispatchers.AppDispatchers
import com.example.core.domain.location.Location
import com.example.core.domain.run.Run
import com.example.core.domain.util.Result
import com.example.presentation.ui.asUiText
import com.example.run.domain.LocationDataCalculator
import com.example.run.domain.RunningTracker
import com.example.run.domain.usecase.UpsertRunUseCase
import com.example.run.presentation.tracking.service.RunTrackingService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.time.ZoneId
import java.time.ZonedDateTime

class RunTrackingViewModel(
    private val runningTracker: RunningTracker,
    private val upsertRunUseCase: UpsertRunUseCase,
    private val appDispatchers: AppDispatchers,
) : ViewModel() {

    var state by mutableStateOf(
        RunTrackingState(
            shouldTrack = RunTrackingService.isServiceActive && runningTracker.isTracking.value,
            hasStartedRunning = RunTrackingService.isServiceActive
        )
    )
        private set

    private val eventChannel = Channel<RunTrackingEvent>()
    val events = eventChannel.receiveAsFlow()

    private val shouldTrack = snapshotFlow { state.shouldTrack }
        .stateIn(viewModelScope, SharingStarted.Lazily, state.shouldTrack)

    private val hasLocationPermission = MutableStateFlow(false)

    private val isTracking = combine(
        shouldTrack,
        hasLocationPermission
    ) { shouldTrack, hasPermission ->
        shouldTrack && hasPermission
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)


    init {
        hasLocationPermission
            .onEach { hasPermission ->
                if (hasPermission) {
                    runningTracker.startObservingLocation()
                } else {
                    runningTracker.stopObservingLocation()
                }
            }.launchIn(viewModelScope)

        isTracking
            .onEach { isTracking ->
                runningTracker.setIsTracking(isTracking)
            }.launchIn(viewModelScope)

        runningTracker
            .currentLocation
            .onEach { location ->
                state = state.copy(
                    currentLocation = location?.location
                )
            }.launchIn(viewModelScope)

        runningTracker
            .runData
            .onEach { runData ->
                state = state.copy(
                    runData = runData
                )
            }.launchIn(viewModelScope)

        runningTracker
            .elapsedTime
            .onEach { elapsedTime ->
                state = state.copy(
                    elapsedTime = elapsedTime
                )
            }.launchIn(viewModelScope)
    }

    fun onAction(action: RunTrackingAction) {
        when (action) {
            RunTrackingAction.OnBackClick -> {
                state = state.copy(
                    shouldTrack = false
                )
                if (state.hasStartedRunning.not()) {
                    viewModelScope.launch {
                        eventChannel.send(RunTrackingEvent.OnBackClick)
                    }
                }
            }

            RunTrackingAction.OnFinishRunClick -> {
                state = state.copy(
                    isRunFinished = true,
                    isSavingRun = true
                )
            }

            RunTrackingAction.OnResumeRunClick -> {
                state = state.copy(
                    shouldTrack = true
                )
            }

            RunTrackingAction.OnToggleRunClick -> {
                state = state.copy(
                    hasStartedRunning = true,
                    shouldTrack = state.shouldTrack.not()
                )
            }

            is RunTrackingAction.SubmitLocationPermissionInfo -> {
                hasLocationPermission.value = action.acceptedLocationPermission
                state = state.copy(showLocationRationale = action.showLocationPermissionRationale)
            }

            is RunTrackingAction.SubmitNotificationPermissionInfo -> {
                state =
                    state.copy(showNotificationRationale = action.showNotificationPermissionRationale)
            }

            RunTrackingAction.DismissRationaleDialog -> {
                state = state.copy(
                    showNotificationRationale = false,
                    showLocationRationale = false
                )
            }

            is RunTrackingAction.OnRunProcessed -> {
                finishRun(action.bitmap)
            }
        }
    }

    private fun finishRun(bitmap: Bitmap) {
        val locations = state.runData.locations
        if (locations.isEmpty() || locations.first().size <= 1) {
            state = state.copy(isSavingRun = false)
            return
        }

        viewModelScope.launch {
            val mapPictureBytes = convertBitmapToByteArray(bitmap)
            val run = Run(
                id = null,
                duration = state.elapsedTime,
                dateTimeUtc = ZonedDateTime.now()
                    .withZoneSameInstant(ZoneId.of("UTC")),
                distanceInMeters = state.runData.distanceMeters,
                location = state.currentLocation ?: Location(0.0, 0.0),
                maxSpeedKmh = LocationDataCalculator.getMaxSpeedKmh(locations),
                totalElevationMeters = LocationDataCalculator.getTotalElevationMeters(locations),
                mapPictureUrl = null
            )
            runningTracker.finishRun()

            Timber.tag("MAP_PICTURE").d("byteArr: $mapPictureBytes")
            when (val result = upsertRunUseCase(run = run, mapPicture = mapPictureBytes)) {
                is Result.Error -> eventChannel.send(RunTrackingEvent.Error(result.error.asUiText()))
                is Result.Success -> eventChannel.send(RunTrackingEvent.RunSuccessfullySaved)
            }

            state = state.copy(isSavingRun = false)
        }
    }

    private suspend fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
        return withContext(appDispatchers.ioDispatcher) {
            val stream = ByteArrayOutputStream()
            stream.use {
                bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    100,
                    it
                )
            }
            stream.toByteArray()
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (RunTrackingService.isServiceActive.not()) {
            runningTracker.stopObservingLocation()
        }
    }
}