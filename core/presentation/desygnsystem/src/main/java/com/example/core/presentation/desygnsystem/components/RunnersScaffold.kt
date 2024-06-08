package com.example.core.presentation.desygnsystem.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RunnersScaffold(
    modifier: Modifier = Modifier,
    withGradient: Boolean = true,
    topAppBar: @Composable () -> Unit = {},
    fab: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = topAppBar,
        floatingActionButton = fab,
        floatingActionButtonPosition = FabPosition.Center,
        modifier = modifier
    ) { paddingValues ->
        if (withGradient) {
            GradientBackground {
                content(paddingValues)
            }
        } else {
            content(paddingValues)
        }
    }
}