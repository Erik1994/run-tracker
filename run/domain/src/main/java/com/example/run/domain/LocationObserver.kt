package com.example.run.domain

import com.example.core.domain.location.LocationWithAltitude
import kotlinx.coroutines.flow.Flow

interface LocationObserver {
    operator fun invoke(interval: Long): Flow<LocationWithAltitude>
}