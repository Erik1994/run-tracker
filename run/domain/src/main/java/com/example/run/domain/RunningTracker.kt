@file:OptIn(
    ExperimentalCoroutinesApi::class
)

package com.example.run.domain

import com.example.core.connectivity.domain.messaging.MessagingAction
import com.example.core.domain.Timer
import com.example.core.domain.location.LocationTimeStamp
import com.example.core.domain.location.LocationWithAltitude
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface RunningTracker {
    val currentLocation: StateFlow<LocationWithAltitude?>
    val runData: StateFlow<RunData>
    val isTracking: StateFlow<Boolean>
    val elapsedTime: StateFlow<Duration>

    fun startObservingLocation()
    fun stopObservingLocation()
    fun setIsTracking(isTracking: Boolean)
    fun finishRun()
}

class RunningTrackerImpl(
    locationObserver: LocationObserver,
    private val applicationScope: CoroutineScope,
    private val watchConnector: WatchConnector
) : RunningTracker {

    private val _ranData = MutableStateFlow(RunData())
    override val runData = _ranData.asStateFlow()

    private val _isTracking = MutableStateFlow(false)
    override val isTracking = _isTracking.asStateFlow()
    private val isObservingLocation = MutableStateFlow(false)

    private val _elapsedTime = MutableStateFlow(Duration.ZERO)
    override val elapsedTime = _elapsedTime.asStateFlow()

    private val heartRates = isTracking
        .flatMapLatest { isTracking ->
            if (isTracking) {
                watchConnector.messagingActions
            } else flowOf()
        }
        .filterIsInstance<MessagingAction.HeartRateUpdate>()
        .map { it.heartRate }
        .runningFold(initial = emptyList<Int>()) { currentHeartRates, newHeartRate ->
            currentHeartRates + newHeartRate
        }
        .stateIn(
            applicationScope,
            SharingStarted.Lazily,
            emptyList()
        )

    override val currentLocation = isObservingLocation
        .flatMapLatest {
            if (it) {
                locationObserver(1000L)
            } else flowOf()
        }
        .stateIn(
            applicationScope,
            SharingStarted.Lazily,
            null
        )

    init {
        isTracking
            .onEach { isTracking ->
                if (isTracking.not()) {
                    val newList = buildList<List<LocationTimeStamp>> {
                        addAll(runData.value.locations)
                        add(emptyList())
                    }.toList()
                    _ranData.update {
                        it.copy(
                            locations = newList
                        )
                    }
                }
            }
            .flatMapLatest {
                if (it) {
                    Timer.timeAndEmit()
                } else flowOf()
            }.onEach { duration ->
                _elapsedTime.value += duration
            }.launchIn(applicationScope)

        currentLocation
            .filterNotNull()
            .combineTransform(isTracking) { location, isTracking ->
                if (isTracking) {
                    emit(location)
                }
            }.zip(_elapsedTime) { location, elapsedTime ->
                LocationTimeStamp(
                    location = location,
                    durationTimestamp = elapsedTime
                )
            }.combine(heartRates) { location, heartRates ->
                val currentLocations = _ranData.value.locations
                val lastLocationsList = if (currentLocations.isNotEmpty()) {
                    currentLocations.last() + location
                } else listOf(location)
                val newLocationsList = currentLocations.replaceLast(lastLocationsList)
                val distanceMeters =
                    LocationDataCalculator.getTotalDistanceMeters(locations = newLocationsList)

                val distanceKm = distanceMeters / 1000.0
                val currentDuration = location.durationTimestamp

                val avgSecondsPerKm = if (distanceKm == 0.0) {
                    0
                } else {
                    (currentDuration.inWholeSeconds / distanceKm).roundToInt()
                }

                _ranData.update {
                    RunData(
                        distanceMeters = distanceMeters,
                        pace = avgSecondsPerKm.seconds,
                        locations = newLocationsList,
                        heartRates = heartRates
                    )
                }
            }.launchIn(applicationScope)

        elapsedTime
            .onEach {
                watchConnector.sendActionToWatch(MessagingAction.TimeUpdate(it))
            }
            .launchIn(applicationScope)

        runData
            .map { it.distanceMeters }
            .distinctUntilChanged()
            .onEach {
                watchConnector.sendActionToWatch(MessagingAction.DistanceUpdate(it))
            }
            .launchIn(applicationScope)
    }

    override fun setIsTracking(isTracking: Boolean) {
        this._isTracking.value = isTracking
    }

    override fun startObservingLocation() {
        isObservingLocation.value = true
        watchConnector.setIsTrackable(true)
    }

    override fun stopObservingLocation() {
        isObservingLocation.value = false
        watchConnector.setIsTrackable(false)
    }

    override fun finishRun() {
        stopObservingLocation()
        setIsTracking(false)
        _elapsedTime.value = Duration.ZERO
        _ranData.value = RunData()
    }
}

private fun <T> List<List<T>>.replaceLast(replacement: List<T>): List<List<T>> {
    if (this.isEmpty()) {
        return listOf(replacement)
    }
    return this.dropLast(1) + listOf(replacement)
}