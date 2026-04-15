package com.wynndie.spwallet.sharedCore.presentation.formatters.input

import androidx.compose.ui.text.input.TextFieldValue
import com.wynndie.spwallet.sharedCore.presentation.formatters.UiText

data class InputFieldState(
    val value: TextFieldValue = TextFieldValue(""),
    val supportingText: UiText? = null,
    val hasError: Boolean = false,
    val minLength: Int = 0,
    val maxLength: Int = 256
)