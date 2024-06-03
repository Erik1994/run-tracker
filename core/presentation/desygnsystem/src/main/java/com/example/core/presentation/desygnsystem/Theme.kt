package com.example.core.presentation.desygnsystem

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.core.presentation.desygnsystem.dimentions.LocalSpacing
import com.example.core.presentation.desygnsystem.dimentions.Spacing

val DarkColorScheme = darkColorScheme(
    primary = RunnersGreen,
    background = RunnersBlack,
    surface = RunnersDarkGray,
    secondaryContainer = RunnersWhite,
    tertiary = RunnersWhite,
    primaryContainer = RunnersGreen30,
    onPrimary = RunnersBlack,
    onBackground = RunnersWhite,
    onSurface = RunnersWhite,
    onSurfaceVariant = RunnersGray
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
    CompositionLocalProvider(LocalSpacing provides Spacing()) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}