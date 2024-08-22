package com.example.wear.run.presentation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.notification.service.RunTrackingService
import com.example.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun TrackerScreenRoot(
    onServiceToggle: (RunTrackingService.RunTrackingServiceState) -> Unit,
    viewModel: TrackerViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.state
    val isServiceActive by RunTrackingService.isServiceActive.collectAsStateWithLifecycle()
    LaunchedEffect(state.isRunActive, state.hasStartedRunning, isServiceActive) {
        if (state.isRunActive && isServiceActive.not()) {
            onServiceToggle(RunTrackingService.RunTrackingServiceState.START)
        }
    }
    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is TrackerEvent.Error -> Toast.makeText(
                context,
                event.message.asString(context),
                Toast.LENGTH_LONG
            ).show()

            TrackerEvent.RunFinished -> {
                onServiceToggle(RunTrackingService.RunTrackingServiceState.STOP)
            }
        }
    }

    TrackerScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}