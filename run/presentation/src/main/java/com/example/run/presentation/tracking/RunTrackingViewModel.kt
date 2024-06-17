package com.example.run.presentation.tracking

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.run.domain.RunningTracker
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import timber.log.Timber

class RunTrackingViewModel(
    private val runningTracker: RunningTracker
) : ViewModel() {

    var state by mutableStateOf(RunTrackingState())
        private set

    private val eventChannel = Channel<RunTrackingEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _hasLocationPermission = MutableStateFlow(false)

    init {
        _hasLocationPermission
            .onEach { hasPermission ->
                if (hasPermission) {
                    runningTracker.startObservingLocation()
                } else {
                    runningTracker.stopObservingLocation()
                }
            }.launchIn(viewModelScope)

        runningTracker
            .currentLocation
            .onEach { location ->
                Timber.d("New Location: $location")
            }.launchIn(viewModelScope)
    }

    fun onAction(action: RunTrackingAction) {
        when (action) {
            RunTrackingAction.OnBackClick -> {}
            RunTrackingAction.OnFinishRunClick -> {}
            RunTrackingAction.OnResumeRunClick -> {}
            RunTrackingAction.OnToggleRunClick -> {}
            is RunTrackingAction.SubmitLocationPermissionInfo -> {
                _hasLocationPermission.value = action.acceptedLocationPermission
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
}