package com.wynndie.spwallet.sharedCore.presentation.model

import com.wynndie.spwallet.sharedCore.presentation.text.UiText

interface DisplayableNumber {
    val value: Number
    val formatted: List<UiText>
}