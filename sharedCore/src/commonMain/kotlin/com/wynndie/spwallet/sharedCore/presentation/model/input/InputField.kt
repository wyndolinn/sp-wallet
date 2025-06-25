package com.wynndie.spwallet.sharedCore.presentation.model.input

import androidx.compose.ui.text.input.TextFieldValue
import com.wynndie.spwallet.sharedCore.presentation.model.UiText

data class InputField(
    val value: TextFieldValue = TextFieldValue(""),
    val suffix: UiText? = null,
    val supportingText: UiText = UiText.DynamicString(""),
    val isError: Boolean = false
)