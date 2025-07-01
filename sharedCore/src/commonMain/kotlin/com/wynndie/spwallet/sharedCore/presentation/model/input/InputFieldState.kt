package com.wynndie.spwallet.sharedCore.presentation.model.input

import androidx.compose.ui.text.input.TextFieldValue
import com.wynndie.spwallet.sharedCore.presentation.model.UiText

data class InputFieldState(
    val value: TextFieldValue = TextFieldValue(""),
    val hasError: Boolean = false,
    val maxLength: Int = 256,
    val supportingText: UiText? = null,
    val prefix: String? = null,
    val suffix: String? = null
)