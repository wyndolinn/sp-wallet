package com.wynndie.spwallet.sharedCore.presentation.serializaers

import com.wynndie.spwallet.sharedCore.presentation.formatters.UiText
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object UiTextSerializer : KSerializer<UiText> {
    override val descriptor = PrimitiveSerialDescriptor("UiText", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: UiText) {
        val text = when (value) {
            is UiText.DynamicString -> value.value
            is UiText.ResourceString -> value.id.toString()
        }
        encoder.encodeString(text)
    }

    override fun deserialize(decoder: Decoder): UiText {
        val string = decoder.decodeString()
        return UiText.DynamicString(string)
    }
}