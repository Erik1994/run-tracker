package com.example.core.presentation.desygnsystem

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val DarkColorScheme = darkColorScheme(
    primary = RunTrackerGreen,
    background = RunTrackerBlack,
    surface = RunTrackerDarkGray,
    secondaryContainer = RunTrackerWhite,
    tertiary = RunTrackerWhite,
    primaryContainer = RunTrackerGreen30,
    onPrimary = RunTrackerBlack,
    onBackground = RunTrackerWhite,
    onSurface = RunTrackerWhite,
    onSurfaceVariant = RunTrackerGray
)

@Composable
fun RunTrackerTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}