package com.wynndie.spwallet.sharedCore.presentation.models

import com.wynndie.spwallet.sharedCore.presentation.extensions.isAlphabet
import com.wynndie.spwallet.sharedCore.presentation.extensions.isHexadecimal
import com.wynndie.spwallet.sharedCore.presentation.extensions.isLatin

enum class InputFilters(val predicate: (Char) -> Boolean) {
    Text({ it.isAlphabet() || it.isDigit() || it.isWhitespace() }),
    Numbers({ it.isDigit() }),
    Uuid({ it.isHexadecimal() || it == '-' }),
    Base64({ it.isDigit() || it.isLatin() || it == '+' || it == '/' || it == '=' })
}