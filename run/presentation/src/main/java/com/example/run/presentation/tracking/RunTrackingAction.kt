package com.example.run.presentation.tracking

import android.graphics.Bitmap

sealed interface RunTrackingAction {

    data object OnToggleRunClick : RunTrackingAction

    data object OnFinishRunClick : RunTrackingAction

    data object OnResumeRunClick : RunTrackingAction

    data object OnBackClick : RunTrackingAction

    data class SubmitLocationPermissionInfo(
        val acceptedLocationPermission: Boolean,
        val showLocationPermissionRationale: Boolean
    ) : RunTrackingAction

    data class SubmitNotificationPermissionInfo(
        val acceptedNotificationPermission: Boolean,
        val showNotificationPermissionRationale: Boolean
    ) : RunTrackingAction

    data object DismissRationaleDialog : RunTrackingAction

    class OnRunProcessed(val bitmap: Bitmap) : RunTrackingAction

}