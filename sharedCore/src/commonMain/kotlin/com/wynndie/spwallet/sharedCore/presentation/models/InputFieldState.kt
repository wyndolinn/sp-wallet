package com.wynndie.spwallet.sharedCore.presentation.formatters

import androidx.compose.ui.text.input.TextFieldValue

data class InputFieldState(
    val value: TextFieldValue = TextFieldValue(""),
    val supportingText: UiText? = null,
    val hasError: Boolean = false,
    val minLength: Int = 0,
    val maxLength: Int = 256
)