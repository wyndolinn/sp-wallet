package com.wynndie.spwallet.sharedtheme.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Sizing(
    val extraExtraSmall: Dp = 24.dp,
    val extraSmall: Dp = 32.dp,
    val small: Dp = 40.dp,
    val medium: Dp = 48.dp,
    val large: Dp = 56.dp,
    val extraLarge: Dp = 64.dp,
    val extraExtraLarge: Dp = 72.dp
)

val LocalSizing = compositionLocalOf { Sizing() }
val MaterialTheme.sizing: Sizing
    @Composable
    @ReadOnlyComposable
    get() = LocalSizing.current