package com.example.core.database.mapper

import com.example.core.database.entity.RunEntity
import com.example.core.domain.location.Location
import com.example.core.domain.run.Run
import org.bson.types.ObjectId
import java.time.Instant
import java.time.ZoneId
import kotlin.time.Duration.Companion.milliseconds

fun RunEntity.toRun(): Run {
    return Run(
        id = id,
        duration = durationMillis.milliseconds,
        dateTimeUtc = Instant.parse(dateTimeUtc)
            .atZone(ZoneId.of("UTC")),
        distanceInMeters = distanceMeters,
        location = Location(
            lat = latitude,
            long = longitude
        ),
        maxSpeedKmh = maxSpeedKmh,
        totalElevationMeters = totalElevationMeters,
        mapPictureUrl = mapPictureUrl,
        avgHeartRate = avgHeartRate,
        maxHeartRate = maxHeartRate
    )
}

fun Run.toRunEntity(): RunEntity {
    return RunEntity(
        id = id ?: ObjectId().toHexString(),
        durationMillis = duration.inWholeMilliseconds,
        maxSpeedKmh = maxSpeedKmh,
        dateTimeUtc = dateTimeUtc.toInstant().toString(),
        latitude = location.lat,
        longitude = location.long,
        distanceMeters = distanceInMeters,
        avgSpeedKmh = avgSpeedKmh,
        totalElevationMeters = totalElevationMeters,
        mapPictureUrl = mapPictureUrl,
        maxHeartRate = maxHeartRate,
        avgHeartRate = avgHeartRate
    )
}