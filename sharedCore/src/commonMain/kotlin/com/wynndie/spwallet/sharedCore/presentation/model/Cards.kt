package com.wynndie.spwallet.sharedCore.presentation.model

import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.recipient
import com.wynndie.spwallet.sharedResources.total_of_ore

val emptyBalance = BlocksDisplayableValue.of(0)

val emptyCashCard = UiCashCard(
    id = "",
    label = UiText.DynamicString(""),
    title = UiText.StringResourceId(Res.string.total_of_ore, emptyBalance.value),
    iconBackground = CardColor.Blue,
    icon = CardIcon.Cash,
    name = "",
    balance = emptyBalance
)

val emptyRecipientCard = UiRecipientCard(
    id = "",
    label = UiText.StringResourceId(Res.string.recipient),
    title = UiText.DynamicString(""),
    iconBackground = CardColor.Blue,
    icon = CardIcon.Person,
    name = "",
    number = ""
)