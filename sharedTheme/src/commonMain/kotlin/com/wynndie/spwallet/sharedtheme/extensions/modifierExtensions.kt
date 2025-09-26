package com.wynndie.spwallet.sharedtheme.extensions

import androidx.compose.ui.Modifier

inline fun Modifier.thenIf(condition: Boolean, block: Modifier.() -> Modifier): Modifier {
    return if (condition) this.block() else this
}

inline fun <T> Modifier.thenIfNull(value: T?, block: Modifier.() -> Modifier): Modifier {
    return if (value == null) this.block() else this
}

inline fun <T> Modifier.thenIfNotNull(value: T?, block: Modifier.(T) -> Modifier): Modifier {
    return if (value != null) this.block(value) else this
}