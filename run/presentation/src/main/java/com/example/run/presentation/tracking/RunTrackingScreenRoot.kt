package com.example.run.presentation.tracking

import androidx.compose.runtime.Composable
import com.example.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun RunTrackingScreenRoot(
    viewModel: RunTrackingViewModel = koinViewModel()
) {

    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is RunTrackingEvent.Error -> {}
            RunTrackingEvent.RunSuccessfullySaved -> {}
        }
    }

    RunTrackingScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}