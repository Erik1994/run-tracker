package com.example.core.presentation.desygnsystem.dimentions

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimensions(
    val dimenDefault: Dp = 0.dp,
    val dimenExtraSmall: Dp = 4.dp,
    val dimenSmall: Dp = 8.dp,
    val dimenMedium: Dp = 16.dp,
    val dimenLarge: Dp = 32.dp,
    val dimenMediumLarge: Dp = 48.dp,
    val dimenExtraLarge: Dp = 64.dp
)

val LocalDimensions = compositionLocalOf { Dimensions() }