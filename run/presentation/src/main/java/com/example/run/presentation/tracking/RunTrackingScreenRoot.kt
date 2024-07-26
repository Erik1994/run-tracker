package com.example.run.presentation.tracking

import androidx.compose.runtime.Composable
import com.example.presentation.ui.ObserveAsEvents
import com.example.run.presentation.tracking.service.RunTrackingService
import org.koin.androidx.compose.koinViewModel

@Composable
fun RunTrackingScreenRoot(
    viewModel: RunTrackingViewModel = koinViewModel(),
    onServiceToggle: (RunTrackingService.RunTrackingServiceState) -> Unit,
) {

    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is RunTrackingEvent.Error -> {}
            RunTrackingEvent.RunSuccessfullySaved -> {}
        }
    }

    RunTrackingScreen(
        state = viewModel.state,
        onServiceToggle = onServiceToggle,
        onAction = viewModel::onAction
    )
}