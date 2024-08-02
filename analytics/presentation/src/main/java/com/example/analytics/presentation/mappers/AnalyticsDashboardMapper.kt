package com.example.analytics.presentation.mappers

import com.example.analytics.domain.AnalyticsValues
import com.example.analytics.presentation.AnalyticsDashboardState
import com.example.presentation.ui.formatted
import com.example.presentation.ui.toFormattedKm
import com.example.presentation.ui.toFormattedKmh
import com.example.presentation.ui.toFormattedTotalTime
import kotlin.time.Duration.Companion.seconds


fun AnalyticsValues.toAnalyticsDashboardState(): AnalyticsDashboardState {
    return AnalyticsDashboardState(
        totalDistanceRun = (totalDistanceRun / 1000.0).toFormattedKm(),
        totalTimeRun = totalTimeRun.toFormattedTotalTime(),
        fastestEverRun = fastestEverRun.toFormattedKmh(),
        avgDistance = (avgDistancePerRun / 1000.0).toFormattedKm(),
        avgPace = avgPacePerRun.seconds.formatted()
    )
}