package com.example.run.presentation.overview

import androidx.compose.runtime.Composable
import com.example.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun RunOverviewScreenRoot(
    onStartRunClick: () -> Unit,
    viewModel: RunOverviewViewModel = koinViewModel(),
) {
    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is RunOverviewEvent.RunTrackingNavigation -> onStartRunClick()
        }
    }
    RunOverviewScreen(
        viewModel.state,
        onAction = viewModel::onAction
    )
}