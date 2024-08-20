package com.example.run.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class RunDto(
    val id: String,
    val durationMillis: Long,
    val distanceMeters: Int,
    val dateTimeUtc: String,
    val lat: Double,
    val long: Double,
    val avgSpeedKmh: Double,
    val maxSpeedKmh: Double,
    val totalElevationMeters: Int,
    val mapPictureUrl: String?,
    val avgHeartRate: Int?,
    val maxHeartRate: Int?
)
