package com.wynndie.spwallet.sharedCore.presentation.extensions

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color


inline fun Modifier.then(other: Modifier.() -> Modifier): Modifier {
    return then(this.other())
}

inline fun Modifier.thenIf(condition: Boolean, other: Modifier.() -> Modifier): Modifier {
    return if (condition) then(this.other()) else then(this)
}

inline fun <T> Modifier.thenIfNotNull(value: T?, other: Modifier.(T) -> Modifier): Modifier {
    return if (value != null) then(this.other(value)) else then(this)
}

fun Modifier.cardColor(color: Color): Modifier {
    return this
        .background(Color.White)
        .background(color.copy(alpha = 0.2f))
}

fun Modifier.shimmerEffect(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmer_translate",
    )

    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim.value - 200f, 0f),
        end = Offset(translateAnim.value, 0f),
    )

    this.background(brush)
}