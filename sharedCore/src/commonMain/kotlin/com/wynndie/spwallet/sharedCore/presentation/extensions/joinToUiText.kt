package com.wynndie.spwallet.sharedCore.presentation.extensions

import androidx.compose.runtime.Composable
import com.wynndie.spwallet.sharedCore.presentation.formatters.UiText

@Composable
fun List<UiText>.joinToUiText(separator: String): UiText.DynamicString {
    @Suppress("SimplifiableCallChain")
    return UiText.DynamicString(this.map { it.asString() }.joinToString(separator))
}