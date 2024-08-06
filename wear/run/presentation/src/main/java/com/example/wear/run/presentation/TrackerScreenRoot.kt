package com.example.wear.run.presentation

import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel

@Composable
fun TrackerScreenRoot(
    viewModel: TrackerViewModel = koinViewModel()
) {

    TrackerScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}