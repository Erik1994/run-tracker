@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.run.presentation.tracking

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.presentation.desygnsystem.RunnersTheme
import com.example.core.presentation.desygnsystem.StartIcon
import com.example.core.presentation.desygnsystem.StopIcon
import com.example.core.presentation.desygnsystem.components.RunnersDialog
import com.example.core.presentation.desygnsystem.components.RunnersFAB
import com.example.core.presentation.desygnsystem.components.RunnersOutlinedActionButton
import com.example.core.presentation.desygnsystem.components.RunnersScaffold
import com.example.core.presentation.desygnsystem.components.toolbar.RunnersToolbar
import com.example.core.presentation.desygnsystem.dimentions.LocalDimensions
import com.example.run.presentation.R
import com.example.run.presentation.tracking.components.RunDataCard
import com.example.run.presentation.tracking.maps.TrackerMap
import com.example.run.presentation.util.hasLocationPermission
import com.example.run.presentation.util.hasNotificationPermission
import com.example.run.presentation.util.shouldShowLocationPermissionRationale
import com.example.run.presentation.util.shouldShowNotificationPermissionRationale

@Composable
fun RunTrackingScreen(
    state: RunTrackingState,
    onAction: (RunTrackingAction) -> Unit
) {
    val dimensions = LocalDimensions.current
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permMap ->

        val hasCoarseLocationPermission =
            permMap.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)
        val hasFineLocationPermission =
            permMap.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)
        val hasNotificationPermission = if (Build.VERSION.SDK_INT >= 33) {
            permMap[Manifest.permission.POST_NOTIFICATIONS] == true
        } else true

        val activity = context as ComponentActivity
        val showLocationRationale = activity.shouldShowLocationPermissionRationale()
        val showNotificationPermissionRationale =
            activity.shouldShowNotificationPermissionRationale()

        onAction(
            RunTrackingAction.SubmitLocationPermissionInfo(
                acceptedLocationPermission = hasFineLocationPermission && hasCoarseLocationPermission,
                showLocationPermissionRationale = showLocationRationale
            )
        )

        onAction(
            RunTrackingAction.SubmitNotificationPermissionInfo(
                acceptedNotificationPermission = hasNotificationPermission,
                showNotificationPermissionRationale = showNotificationPermissionRationale
            )
        )
    }

    // Handle the case when a user first time declines the permission and
    // the rational dialog is shown and the user kills the app without requesting
    // the permissions second time. As showRational is true when user declines
    // the permissions first time, in this case we when user opens app again
    // we should show rational dialog immediately
    LaunchedEffect(key1 = true) {
        val activity = context as ComponentActivity
        val showLocationRationale = activity.shouldShowLocationPermissionRationale()
        val showNotificationRationale = activity.shouldShowNotificationPermissionRationale()

        onAction(
            RunTrackingAction.SubmitLocationPermissionInfo(
                acceptedLocationPermission = context.hasLocationPermission(),
                showLocationPermissionRationale = showLocationRationale
            )
        )

        onAction(
            RunTrackingAction.SubmitNotificationPermissionInfo(
                acceptedNotificationPermission = context.hasNotificationPermission(),
                showNotificationPermissionRationale = showNotificationRationale
            )
        )

        if (showLocationRationale.not() && showNotificationRationale.not()) {
            permissionLauncher.requestRunnersPermissions(context)
        }
    }

    RunnersScaffold(
        withGradient = false,
        topAppBar = {
            RunnersToolbar(
                showBackButton = true,
                title = stringResource(id = R.string.active_run),
                onBackClick = {
                    onAction(RunTrackingAction.OnBackClick)
                },
            )
        },
        fab = {
            RunnersFAB(
                icon = if (state.shouldTrack) StopIcon else StartIcon,
                onCLick = { onAction(RunTrackingAction.OnToggleRunClick) },
                iconSize = 20.dp,
                contentDescription = if (state.shouldTrack) {
                    stringResource(id = R.string.pause_run)
                } else {
                    stringResource(id = R.string.start_run)
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            TrackerMap(
                isRunFinished = state.isRunFinished,
                currentLocation = state.currentLocation,
                locations = state.runData.locations,
                onSnapshot = {},
                modifier = Modifier.fillMaxSize()
            )
            RunDataCard(
                elapsedTime = state.elapsedTime,
                runData = state.runData,
                modifier = Modifier
                    .padding(dimensions.dimenMedium)
                    .padding(padding)
                    .fillMaxWidth()
            )
        }
    }

    if (state.showLocationRationale || state.showNotificationRationale) {
        RunnersDialog(
            title = stringResource(id = R.string.permission_required),
            description = when {
                state.showNotificationRationale && state.showLocationRationale -> stringResource(
                    id = R.string.location_notification_rationale
                )

                state.showLocationRationale -> stringResource(id = R.string.location_rationale)
                else -> stringResource(id = R.string.notification_rationale)
            },
            onDismiss = { /* We don't want to dismiss as it for permission */ },
            primaryButton = {
                RunnersOutlinedActionButton(
                    text = stringResource(id = R.string.okay),
                    isLoading = false,
                    onClick = {
                        onAction(RunTrackingAction.DismissRationaleDialog)
                        permissionLauncher.requestRunnersPermissions(context = context)
                    }
                )
            }
        )
    }
}

private fun ActivityResultLauncher<Array<String>>.requestRunnersPermissions(
    context: Context
) {
    val hasLocationPermission = context.hasLocationPermission()
    val hasNotificationPermission = context.hasNotificationPermission()

    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    val notificationPermission = if (Build.VERSION.SDK_INT >= 33) {
        arrayOf(Manifest.permission.POST_NOTIFICATIONS)
    } else arrayOf()

    when {
        hasLocationPermission.not() && hasNotificationPermission.not() -> launch(
            locationPermissions + notificationPermission
        )
        hasLocationPermission.not() -> launch(locationPermissions)
        hasNotificationPermission.not() -> launch(notificationPermission)
    }
}


@Preview
@Composable
fun RunTrackingScreenPreview() {
    RunnersTheme {
        RunTrackingScreen(
            state = RunTrackingState(),
            onAction = {}
        )
    }
}