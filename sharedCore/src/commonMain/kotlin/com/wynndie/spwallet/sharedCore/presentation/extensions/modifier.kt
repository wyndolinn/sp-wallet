package com.wynndie.spwallet.sharedCore.presentation.extensions

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
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

fun Modifier.cardColor(color: Color): Modifier {
    return this
        .background(Color.White)
        .background(color.copy(alpha = 0.2f))
}