package com.example.wear.run.presentation

sealed interface TrackerEvent {
    data object RunFinished : TrackerEvent
}