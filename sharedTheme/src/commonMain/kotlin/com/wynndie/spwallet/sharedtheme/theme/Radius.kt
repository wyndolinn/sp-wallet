package com.wynndie.spwallet.sharedtheme.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.dp

data class Radius(
    val default: CornerBasedShape = RoundedCornerShape(16.dp),
    val extraSmall: CornerBasedShape = RoundedCornerShape(4.dp),
    val small: CornerBasedShape = RoundedCornerShape(8.dp),
    val medium: CornerBasedShape = RoundedCornerShape(16.dp),
    val large: CornerBasedShape = RoundedCornerShape(24.dp),
    val extraLarge: CornerBasedShape = RoundedCornerShape(32.dp),
    val circle: CornerBasedShape = CircleShape
)

val LocalRadius = compositionLocalOf { Radius() }
val MaterialTheme.radius: Radius
    @Composable
    @ReadOnlyComposable
    get() = LocalRadius.current