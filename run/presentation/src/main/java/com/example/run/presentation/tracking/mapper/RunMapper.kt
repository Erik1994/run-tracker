package com.example.run.presentation.tracking.mapper

import com.example.core.domain.run.Run
import com.example.presentation.ui.formatted
import com.example.presentation.ui.toFormattedHeartRate
import com.example.presentation.ui.toFormattedKm
import com.example.presentation.ui.toFormattedKmh
import com.example.presentation.ui.toFormattedMeters
import com.example.presentation.ui.toFormattedPace
import com.example.run.presentation.tracking.model.RunUi
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Run.toRunUi(): RunUi {
    val dateTimeInLocalTime = dateTimeUtc
        .withZoneSameInstant(ZoneId.systemDefault())
    val formattedDateTime = DateTimeFormatter
        .ofPattern("MMM dd, yyyy - hh:mma")
        .format(dateTimeInLocalTime)
    val distanceKm = distanceInMeters / 1000.0

    return RunUi(
        id = id.orEmpty(),
        duration = duration.formatted(),
        dateTime = formattedDateTime,
        distance = distanceKm.toFormattedKm(),
        avgSpeed = avgSpeedKmh.toFormattedKmh(),
        maxSpeed = avgSpeedKmh.toFormattedKmh(),
        pace = duration.toFormattedPace(distanceKm),
        totalElevation = totalElevationMeters.toFormattedMeters(),
        mapPictureUrl = mapPictureUrl,
        avgHeartRate = avgHeartRate.toFormattedHeartRate(),
        maxHeartRate = maxHeartRate.toFormattedHeartRate()
    )
}