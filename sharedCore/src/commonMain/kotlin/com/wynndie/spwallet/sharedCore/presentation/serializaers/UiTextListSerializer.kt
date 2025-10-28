package com.wynndie.spwallet.sharedCore.presentation.serializaers

import com.wynndie.spwallet.sharedCore.presentation.formatters.UiText
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object UiTextListSerializer : KSerializer<List<UiText>> {
    private val elementSerializer = UiTextSerializer
    private val listSerializer = ListSerializer(elementSerializer)
    override val descriptor = listSerializer.descriptor

    override fun serialize(encoder: Encoder, value: List<UiText>) {
        return listSerializer.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): List<UiText> {
        return listSerializer.deserialize(decoder)
    }
}