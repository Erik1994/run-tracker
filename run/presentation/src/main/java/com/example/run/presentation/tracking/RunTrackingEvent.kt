package com.example.run.presentation.tracking

import com.example.presentation.ui.UiText

sealed interface RunTrackingEvent {

    data class Error(val uiText: UiText) : RunTrackingEvent

    data object RunSuccessfullySaved : RunTrackingEvent
}