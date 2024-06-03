package com.example.core.presentation.desygnsystem.dimentions

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Spacing(
    val spaceDefault: Dp = 0.dp,
    val spaceExtraSmall: Dp = 4.dp,
    val spaceSmall: Dp = 8.dp,
    val spaceMedium: Dp = 16.dp,
    val spaceLarge: Dp = 32.dp,
    val spaceMediumLarge: Dp = 48.dp,
    val spaceExtraLarge: Dp = 64.dp
)

val LocalSpacing = compositionLocalOf { Spacing() }