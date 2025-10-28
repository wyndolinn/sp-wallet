package com.wynndie.spwallet.sharedCore.presentation.serializaers

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object TextFieldValueSerializer : KSerializer<TextFieldValue> {
    override val descriptor = buildClassSerialDescriptor("TextFieldValue") {
        element<String>("text")
        element<Int>("selectionStart")
        element<Int>("selectionEnd")
    }

    override fun serialize(encoder: Encoder, value: TextFieldValue) {
        val composite = encoder.beginStructure(descriptor)
        composite.encodeStringElement(descriptor, 0, value.text)
        composite.encodeIntElement(descriptor, 1, value.selection.start)
        composite.encodeIntElement(descriptor, 2, value.selection.end)
        composite.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): TextFieldValue {
        val dec = decoder.beginStructure(descriptor)
        var text = ""
        var start = 0
        var end = 0

        loop@ while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                0 -> text = dec.decodeStringElement(descriptor, 0)
                1 -> start = dec.decodeIntElement(descriptor, 1)
                2 -> end = dec.decodeIntElement(descriptor, 2)
                CompositeDecoder.DECODE_DONE -> break@loop
                else -> error("Unexpected index: $index")
            }
        }

        dec.endStructure(descriptor)
        return TextFieldValue(text, TextRange(start, end))
    }
}