@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalCoroutinesApi::class,
    ExperimentalCoroutinesApi::class
)

package com.example.run.domain.location

import com.example.core.domain.location.LocationWithAltitude
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

interface RunningTracker {
    val currentLocation: Flow<LocationWithAltitude?>
    fun startObservingLocation()
    fun stopObservingLocation()
}

class RunningTrackerImpl(
    locationObserver: LocationObserver,
    private val applicationScope: CoroutineScope
): RunningTracker {

    private val isObservingLocation = MutableStateFlow(false)

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

    override fun startObservingLocation() {
        isObservingLocation.value = true
    }

    override fun stopObservingLocation() {
        isObservingLocation.value = false
    }
}