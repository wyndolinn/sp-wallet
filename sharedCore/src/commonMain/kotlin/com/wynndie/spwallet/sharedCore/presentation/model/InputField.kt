package com.wynndie.spwallet.sharedCore.presentation.model

import androidx.compose.ui.text.input.TextFieldValue

data class InputField(
    val value: TextFieldValue = TextFieldValue(""),
    val suffix: UiText? = null,
    val supportingText: UiText = UiText.DynamicString(""),
    val isError: Boolean = false
)