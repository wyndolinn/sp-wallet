package com.wynndie.spwallet.sharedCore.presentation.mapper

import androidx.compose.runtime.Composable
import com.wynndie.spwallet.sharedCore.presentation.model.UiText

@Composable
fun List<UiText>.joinToUiText(separator: String = ", "): UiText.DynamicString {
    return UiText.DynamicString(this.map { it.asString() }.joinToString(separator))
}