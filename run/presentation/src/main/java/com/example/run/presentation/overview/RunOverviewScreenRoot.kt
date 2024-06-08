package com.example.run.presentation.overview

import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel

@Composable
fun RunOverviewScreenRoot(
    viewModel: RunOverviewViewModel = koinViewModel(),
) {
    RunOverviewScreen(
        onAction = viewModel::onAction
    )
}