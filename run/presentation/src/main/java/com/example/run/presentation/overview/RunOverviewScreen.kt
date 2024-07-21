@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.run.presentation.overview

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.presentation.desygnsystem.AnalyticsIcon
import com.example.core.presentation.desygnsystem.LogoIcon
import com.example.core.presentation.desygnsystem.LogoutIcon
import com.example.core.presentation.desygnsystem.RunIcon
import com.example.core.presentation.desygnsystem.RunnersTheme
import com.example.core.presentation.desygnsystem.components.RunnersFAB
import com.example.core.presentation.desygnsystem.components.RunnersScaffold
import com.example.core.presentation.desygnsystem.components.toolbar.DropDownItem
import com.example.core.presentation.desygnsystem.components.toolbar.RunnersToolbar
import com.example.run.presentation.R

@Composable
fun RunOverviewScreen(
    onAction: (RunOverviewAction) -> Unit
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = topAppBarState
    )
    RunnersScaffold(
        topAppBar = {
            RunnersToolbar(
                showBackButton = false,
                title = stringResource(id = R.string.runners),
                scrollBehavior = scrollBehavior,
                startContent = {
                    Icon(
                        imageVector = LogoIcon, contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(30.dp)
                    )
                },
                menuItems = listOf(
                    DropDownItem(
                        icon = AnalyticsIcon,
                        title = stringResource(id = R.string.analytics)
                    ),
                    DropDownItem(
                        icon = LogoutIcon,
                        title = stringResource(id = R.string.logut)
                    )
                ),
                onMenuItemClick = { index ->
                    when (index) {
                        0 -> onAction(RunOverviewAction.OnAnalyticsClick)
                        1 -> onAction(RunOverviewAction.OnLogoutClick)
                    }
                }
            )
        },
        fab = {
            RunnersFAB(
                icon = RunIcon,
                onCLick = {
                    onAction(RunOverviewAction.OnStartClick)
                }
            )
        }
    ) { padding ->

    }
}


@Preview
@Composable
fun RunOverviewScreenPreview() {
    RunnersTheme {
        RunOverviewScreen(onAction = {})
    }
}