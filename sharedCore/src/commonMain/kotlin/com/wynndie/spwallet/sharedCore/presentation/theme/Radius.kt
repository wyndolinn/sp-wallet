package com.wynndie.spwallet.sharedCore.presentation.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.dp

data class Radius(
    val default: RoundedCornerShape = RoundedCornerShape(16.dp),
    val extraSmall: RoundedCornerShape = RoundedCornerShape(4.dp),
    val small: RoundedCornerShape = RoundedCornerShape(8.dp),
    val medium: RoundedCornerShape = RoundedCornerShape(16.dp),
    val large: RoundedCornerShape = RoundedCornerShape(24.dp),
    val extraLarge: RoundedCornerShape = RoundedCornerShape(32.dp),
    val circle: RoundedCornerShape = CircleShape
)

val LocalRadius = compositionLocalOf { Radius() }
val MaterialTheme.radius: Radius
    @Composable
    @ReadOnlyComposable
    get() = LocalRadius.current