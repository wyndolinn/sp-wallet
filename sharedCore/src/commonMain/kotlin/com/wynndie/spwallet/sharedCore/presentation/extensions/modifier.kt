package com.wynndie.spwallet.sharedCore.presentation.extensions

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

inline fun Modifier.thenIf(condition: Boolean, block: Modifier.() -> Modifier): Modifier {
    return if (condition) then(this.block()) else then(this)
}

inline fun <T> Modifier.thenIfNull(value: T?, block: Modifier.() -> Modifier): Modifier {
    return if (value == null) then(this.block()) else then(this)
}

inline fun <T> Modifier.thenIfNotNull(value: T?, block: Modifier.(T) -> Modifier): Modifier {
    return if (value != null) then(this.block(value)) else then(this)
}

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

fun Modifier.tileShadow(shape: CornerBasedShape, color: Color = Color.Black): Modifier {
    return this.dropShadow(
        shape = shape,
        shadow = Shadow(
            radius = 30.dp,
            color = color,
            alpha = 0.1f
        )
    )
}