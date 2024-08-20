package com.example.run.presentation.tracking.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.presentation.desygnsystem.RunnersTheme
import com.example.core.presentation.desygnsystem.dimentions.LocalDimensions
import com.example.presentation.ui.formatted
import com.example.presentation.ui.toFormattedHeartRate
import com.example.presentation.ui.toFormattedKm
import com.example.presentation.ui.toFormattedPace
import com.example.run.domain.RunData
import com.example.run.presentation.R
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes


@Composable
fun RunDataCard(
    elapsedTime: Duration,
    runData: RunData,
    modifier: Modifier = Modifier
) {
    val dimensions = LocalDimensions.current
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(dimensions.dimenMedium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RunDataItem(
            title = stringResource(R.string.duration),
            value = elapsedTime.formatted(),
            valueFontSize = 32.sp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            RunDataItem(
                title = stringResource(R.string.distance),
                value = (runData.distanceMeters / 1000.0).toFormattedKm(),
                modifier = Modifier
                    .defaultMinSize(minWidth = 75.dp)
            )
            RunDataItem(
                title = stringResource(R.string.heart_rate),
                value = runData.heartRates.lastOrNull().toFormattedHeartRate(),
                modifier = Modifier
                    .defaultMinSize(minWidth = 75.dp)
            )
            RunDataItem(
                title = stringResource(R.string.pace),
                value = elapsedTime.toFormattedPace(
                    (runData.distanceMeters / 1000.0)
                ),
                modifier = Modifier
                    .defaultMinSize(minWidth = 75.dp)
            )
        }
    }
}


@Preview
@Composable
fun RunDataCardPreview() {
    RunnersTheme {
        RunDataCard(
            elapsedTime = 10.minutes,
            runData = RunData(
                distanceMeters = 2700,
                pace = 3.minutes
            )
        )
    }
}