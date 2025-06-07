package com.wynndie.spwallet.sharedCore.presentation.mapper

import androidx.compose.runtime.Composable
import com.wynndie.spwallet.sharedCore.presentation.model.UiText

@Composable
fun List<UiText>.joinAsString(separator: String = ", "): String {
    return this.map { it.asString() }.joinToString(separator)
}