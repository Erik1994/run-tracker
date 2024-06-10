package com.example.run.presentation.tracking

import com.example.core.domain.location.Location
import com.example.run.domain.RunData
import kotlin.time.Duration

data class RunTrackingState (
    val elapsedTime: Duration = Duration.ZERO,
    val shouldTrack: Boolean = false,
    val hasStartedRunning: Boolean = false,
    val currentLocation: Location? = null,
    val isRunFinished: Boolean = false,
    val isSavingRun: Boolean = false,
    val runData: RunData = RunData(),
    val showLocationRationale: Boolean = false,
    val showNotificationRationale: Boolean = false,
)