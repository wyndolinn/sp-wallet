package com.wynndie.spwallet.sharedtheme.extensions

import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color

@Composable
fun ContentState.asLabelColor(): Color {
    val color by animateColorAsState(
        targetValue = when (this) {
            ContentState.Alerted -> MaterialTheme.colorScheme.error
            ContentState.Disabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            ContentState.Neutral -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            ContentState.Selected -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        }
    )
    return color
}

@Composable
fun ContentState.asTitleColor(): Color {
    val color by animateColorAsState(
        targetValue = when (this) {
            ContentState.Alerted -> MaterialTheme.colorScheme.error
            ContentState.Disabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            ContentState.Neutral -> MaterialTheme.colorScheme.onSurface
            ContentState.Selected -> MaterialTheme.colorScheme.primary
        }
    )
    return color
}

@Composable
fun ContentState.asDescriptionColor(): Color {
    val color by animateColorAsState(
        targetValue = when (this) {
            ContentState.Alerted -> MaterialTheme.colorScheme.error
            ContentState.Disabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            ContentState.Neutral -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            ContentState.Selected -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        }
    )
    return color
}