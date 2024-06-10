package com.example.run.presentation.tracking

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class RunTrackingViewModel : ViewModel() {

    var state by mutableStateOf(RunTrackingState())
        private set

    private val eventChannel = Channel<RunTrackingEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _hasLocationPermission = MutableStateFlow(false)

    fun onAction(action: RunTrackingAction) {
        when(action) {
            RunTrackingAction.OnBackClick -> {}
            RunTrackingAction.OnFinishRunClick -> {}
            RunTrackingAction.OnResumeRunClick -> {}
            RunTrackingAction.OnToggleRunClick -> {}
            is RunTrackingAction.SubmitLocationPermissionInfo -> {
                _hasLocationPermission.value = action.acceptedLocationPermission
                state = state.copy(showLocationRationale = action.showLocationPermissionRationale)
            }
            is RunTrackingAction.SubmitNotificationPermissionInfo -> {
                state = state.copy(showNotificationRationale = action.showNotificationPermissionRationale)
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