@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.run.presentation.tracking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.presentation.desygnsystem.RunnersTheme
import com.example.core.presentation.desygnsystem.StartIcon
import com.example.core.presentation.desygnsystem.StopIcon
import com.example.core.presentation.desygnsystem.components.RunnersFAB
import com.example.core.presentation.desygnsystem.components.RunnersScaffold
import com.example.core.presentation.desygnsystem.components.toolbar.RunnersToolbar
import com.example.core.presentation.desygnsystem.dimentions.LocalDimensions
import com.example.run.presentation.R
import com.example.run.presentation.tracking.components.RunDataCard

@Composable
fun RunTrackingScreen(
    state: RunTrackingState,
    onAction: (RunTrackingAction) -> Unit
) {
    val dimensions = LocalDimensions.current
    RunnersScaffold(
        withGradient = false,
        topAppBar = {
            RunnersToolbar(
                showBackButton = true,
                title = stringResource(id = R.string.active_run),
                onBackClick = {
                    onAction(RunTrackingAction.OnBackClick)
                },
            )
        },
        fab = {
            RunnersFAB(
                icon = if (state.shouldTrack) StopIcon else StartIcon,
                onCLick = { onAction(RunTrackingAction.OnToggleRunClick) },
                iconSize = 20.dp,
                contentDescription = if (state.shouldTrack) {
                    stringResource(id = R.string.pause_run)
                } else {
                    stringResource(id = R.string.start_run)
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            RunDataCard(
                elapsedTime = state.elapsedTime,
                runData = state.runData,
                modifier = Modifier
                    .padding(dimensions.dimenMedium)
                    .padding(padding)
                    .fillMaxWidth()
            )
        }
    }
}


@Preview
@Composable
fun RunTrackingScreenPreview() {
    RunnersTheme {
        RunTrackingScreen(
            state = RunTrackingState(),
            onAction = {}
        )
    }
}