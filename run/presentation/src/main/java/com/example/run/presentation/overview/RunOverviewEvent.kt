package com.example.run.presentation.overview

interface RunOverviewEvent {
    data object RunTrackingNavigationEvent : RunOverviewEvent
    data object AuthNavigationEvent : RunOverviewEvent
}