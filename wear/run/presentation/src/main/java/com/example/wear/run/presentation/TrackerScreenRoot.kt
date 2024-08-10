package com.example.wear.run.presentation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun TrackerScreenRoot(
    viewModel: TrackerViewModel = koinViewModel()
) {
    val context = LocalContext.current
    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is TrackerEvent.Error -> Toast.makeText(
                context,
                event.message.asString(context),
                Toast.LENGTH_LONG
            ).show()
            TrackerEvent.RunFinished -> Unit
        }
    }

    TrackerScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}