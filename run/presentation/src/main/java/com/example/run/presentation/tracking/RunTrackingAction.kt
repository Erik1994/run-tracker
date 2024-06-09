package com.example.run.presentation.tracking

sealed interface RunTrackingAction {

    data object OnToggleRunClick : RunTrackingAction

    data object OnFinishRunClick : RunTrackingAction

    data object OnResumeRunClick : RunTrackingAction

    data object OnBackClick : RunTrackingAction

}