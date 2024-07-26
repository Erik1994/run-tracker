package com.example.run.presentation.tracking

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.run.domain.RunningTracker
import com.example.run.presentation.tracking.service.RunTrackingService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn

class RunTrackingViewModel(
    private val runningTracker: RunningTracker
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
            }

            RunTrackingAction.OnFinishRunClick -> {

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
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (RunTrackingService.isServiceActive.not()) {
            runningTracker.stopObservingLocation()
        }
    }
}