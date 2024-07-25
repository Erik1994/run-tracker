@file:OptIn(
    ExperimentalCoroutinesApi::class, ExperimentalCoroutinesApi::class,
    ExperimentalCoroutinesApi::class, ExperimentalCoroutinesApi::class,
    ExperimentalCoroutinesApi::class
)

package com.example.run.domain

import com.example.core.domain.Timer
import com.example.core.domain.location.LocationTimeStamp
import com.example.core.domain.location.LocationWithAltitude
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface RunningTracker {

    val currentLocation: StateFlow<LocationWithAltitude?>

    val runData: StateFlow<RunData>

    val elapsedTime: StateFlow<Duration>
    fun startObservingLocation()
    fun stopObservingLocation()

    fun setIsTracking(isTracking: Boolean)
}

class RunningTrackerImpl(
    locationObserver: LocationObserver,
    private val applicationScope: CoroutineScope
) : RunningTracker {

    private val _ranData = MutableStateFlow(RunData())
    override val runData = _ranData.asStateFlow()

    private val isTracking = MutableStateFlow(false)
    private val isObservingLocation = MutableStateFlow(false)

    private val _elapsedTime = MutableStateFlow(Duration.ZERO)
    override val elapsedTime = _elapsedTime.asStateFlow()

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
            }.onEach { location ->
                val currentLocations = _ranData.value.locations
                val lastLocationsList = if (currentLocations.isNotEmpty()) {
                    currentLocations.last() + location
                } else listOf(location)
                val newLocationsList = currentLocations.replaceLast(lastLocationsList)
                val distanceMeters = LocationDataCalculator.getTotalDistanceMeters(locations = newLocationsList)

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
                        locations = newLocationsList
                    )
                }
            }.launchIn(applicationScope)
    }

    override fun setIsTracking(isTracking: Boolean) {
        this.isTracking.value = isTracking
    }
    override fun startObservingLocation() {
        isObservingLocation.value = true
    }

    override fun stopObservingLocation() {
        isObservingLocation.value = false
    }
}

private fun <T> List<List<T>>.replaceLast(replacement: List<T>): List<List<T>> {
    if (this.isEmpty()) {
        return listOf(replacement)
    }
    return this.dropLast(1) + listOf(replacement)
}