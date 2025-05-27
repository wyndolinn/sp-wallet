package com.wynndie.spwallet.sharedFeature.wallet.presentation.model

import com.wynndie.spwallet.sharedCore.presentation.text.UiText
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.recipient
import com.wynndie.spwallet.sharedResources.total_of_ore

internal val emptyBalance = BlocksDisplayableValue.of(0)

internal val emptyCashCard = UiCashCard(
    id = "",
    label = UiText.DynamicString(""),
    title = UiText.StringResourceId(Res.string.total_of_ore, emptyBalance.value),
    iconBackground = CardColor.from(0),
    icon = CardIcon.from(0),
    name = "",
    balance = emptyBalance
)

internal val emptyRecipientCard = UiRecipientCard(
    id = "",
    label = UiText.StringResourceId(Res.string.recipient),
    title = UiText.DynamicString(""),
    description = UiText.DynamicString(""),
    iconBackground = CardColor.from(0),
    icon = CardIcon.from(0),
    name = "",
    number = ""
)