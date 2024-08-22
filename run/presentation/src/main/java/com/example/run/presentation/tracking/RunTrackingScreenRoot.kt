package com.example.run.presentation.tracking

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.core.notification.service.RunTrackingService
import com.example.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun RunTrackingScreenRoot(
    viewModel: RunTrackingViewModel = koinViewModel(),
    onServiceToggle: (RunTrackingService.RunTrackingServiceState) -> Unit,
    onFinish: () -> Unit,
    onBack: () -> Unit,
) {
    val context = LocalContext.current

    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is RunTrackingEvent.Error -> Toast.makeText(
                context,
                event.uiText.asString(context),
                Toast.LENGTH_LONG
            ).show()
            RunTrackingEvent.RunSuccessfullySaved -> onFinish()
            RunTrackingEvent.OnBackClick -> onBack()
        }
    }

    RunTrackingScreen(
        state = viewModel.state,
        onServiceToggle = onServiceToggle,
        onAction = viewModel::onAction
    )
}