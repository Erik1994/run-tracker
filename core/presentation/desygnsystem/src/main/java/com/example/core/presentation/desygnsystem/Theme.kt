package com.example.core.presentation.desygnsystem

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.core.presentation.desygnsystem.dimentions.LocalDimensions
import com.example.core.presentation.desygnsystem.dimentions.Dimensions

val DarkColorScheme = darkColorScheme(
    primary = RunnersGreen,
    background = RunnersBlack,
    surface = RunnersDarkGray,
    secondary = RunnersWhite,
    tertiary = RunnersWhite,
    primaryContainer = RunnersGreen30,
    onPrimary = RunnersBlack,
    onBackground = RunnersWhite,
    onSurface = RunnersWhite,
    onSurfaceVariant = RunnersGray,
    error = RunnersDarkRed,
    errorContainer = RunnersDarkRed5
)

@Composable
fun RunnersTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Icons in status bar will be shown as light icons
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }
    CompositionLocalProvider(LocalDimensions provides Dimensions()) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}