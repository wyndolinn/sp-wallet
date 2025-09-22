package com.wynndie.spwallet.sharedCore.presentation.models

import androidx.compose.ui.text.input.TextFieldValue
import com.wynndie.spwallet.sharedCore.presentation.formatters.UiText

data class InputField(
    val value: TextFieldValue = TextFieldValue(""),
    val hasError: Boolean = false,
    val maxLength: Int = 256,
    val supportingText: UiText? = null,
    val prefix: String? = null,
    val suffix: String? = null
)