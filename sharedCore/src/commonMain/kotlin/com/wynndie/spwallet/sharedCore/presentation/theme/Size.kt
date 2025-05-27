package com.wynndie.spwallet.sharedCore.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Size(
    val default: Dp = 48.dp,
    val extraSmall: Dp = 24.dp,
    val small: Dp = 32.dp,
    val medium: Dp = 40.dp,
    val large: Dp = 48.dp,
    val extraLarge: Dp = 56.dp
) {
    fun scale(factor: Float): Size {
        return Size(
            extraSmall =  extraSmall * factor,
            small = small * factor,
            medium = medium * factor,
            large = large * factor,
            extraLarge = extraLarge * factor
        )
    }
}

val LocalSize = compositionLocalOf { Size() }

val MaterialTheme.size: Size
    @Composable
    @ReadOnlyComposable
    get() = LocalSize.current