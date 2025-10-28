package com.wynndie.spwallet.sharedCore.presentation.states

import androidx.compose.ui.text.input.TextFieldValue
import com.wynndie.spwallet.sharedCore.presentation.formatters.UiText
import com.wynndie.spwallet.sharedCore.presentation.serializaers.TextFieldValueSerializer
import com.wynndie.spwallet.sharedCore.presentation.serializaers.UiTextSerializer
import kotlinx.serialization.Serializable

@Serializable
data class InputFieldState(

    @Serializable(with = TextFieldValueSerializer::class)
    val value: TextFieldValue = TextFieldValue(""),

    @Serializable(with = UiTextSerializer::class)
    val supportingText: UiText? = null,

    val hasError: Boolean = false,
    val maxLength: Int = 256
)