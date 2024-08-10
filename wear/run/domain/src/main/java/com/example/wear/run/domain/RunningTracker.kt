@file:Suppress("OPT_IN_USAGE")

package com.example.wear.run.domain

import com.example.core.connectivity.domain.messaging.MessagingAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration

interface RunningTracker {
    val heartRate: StateFlow<Int>
    val distanceMeters: StateFlow<Int>
    val isTracking: StateFlow<Boolean>
    val isTrackable: StateFlow<Boolean>
    val elapsedTime : StateFlow<Duration>
    fun setIsTracking(isTracking: Boolean)
}

class RunningTrackerImpl(
    private val watchToPhoneConnector: PhoneConnector,
    private val exerciseTracker: ExerciseTracker,
    applicationScope: CoroutineScope
): RunningTracker {

    private val _heartRate = MutableStateFlow(0)
    private val _isTracking = MutableStateFlow(false)
    private val _isTrackable = MutableStateFlow(false)

    override val heartRate: StateFlow<Int> = _heartRate.asStateFlow()
    override val isTrackable: StateFlow<Boolean> = _isTrackable.asStateFlow()
    override val isTracking: StateFlow<Boolean> = _isTracking.asStateFlow()
    override val distanceMeters: StateFlow<Int> = watchToPhoneConnector
        .messagingActions
        .filterIsInstance<MessagingAction.DistanceUpdate>()
        .map { it.distanceMeters }
        .stateIn(
            applicationScope,
            SharingStarted.Lazily,
            0
        )
    override val elapsedTime: StateFlow<Duration> = watchToPhoneConnector
        .messagingActions
        .filterIsInstance<MessagingAction.TimeUpdate>()
        .map { it.elapsedDuration }
        .stateIn(
            applicationScope,
            SharingStarted.Lazily,
            Duration.ZERO
        )

    init {
        watchToPhoneConnector
            .messagingActions
            .onEach { action ->
                when(action) {
                    MessagingAction.Trackable -> _isTrackable.value = true
                    MessagingAction.Untrackable -> _isTrackable.value = false
                    else -> Unit
                }
            }
            .launchIn(applicationScope)

        watchToPhoneConnector
            .connectedNode
            .filterNotNull()
            .onEach {
                exerciseTracker.prepareExercise()
            }
            .launchIn(applicationScope)

        isTracking
            .flatMapLatest { isTracking ->
                if (isTracking) {
                    exerciseTracker.heartRate
                } else flowOf()
            }
            .onEach { heartRate ->
                watchToPhoneConnector.sendActionToPhone(MessagingAction.HeartRateUpdate(heartRate))
                _heartRate.value = heartRate
            }
            .launchIn(applicationScope)

    }

    override fun setIsTracking(isTracking: Boolean) {
        this._isTracking.value = isTracking
    }
}