package com.wynndie.spwallet.sharedCore.presentation.extensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.cardColorGradient(color: Color): Modifier {
    val darkCoefficient = 0.25f
    val darkerColor = color.copy(
        red = color.red * (1 - darkCoefficient),
        green = color.green * (1 - darkCoefficient),
        blue = color.blue * (1 - darkCoefficient)
    )

    return this.drawBehind {
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(darkerColor, color),
                center = Offset(size.width, size.height * 1.5f),
                radius = size.width
            )
        )
    }
}