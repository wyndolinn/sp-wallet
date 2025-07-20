package com.wynndie.spwallet.sharedCore.presentation.model

import com.wynndie.spwallet.sharedCore.presentation.model.card.CardColor
import com.wynndie.spwallet.sharedCore.presentation.model.card.CardIcon
import com.wynndie.spwallet.sharedCore.presentation.model.card.UiCashCard

val emptyBalance = BlocksDisplayableValue.of(0)

val emptyCashCard = UiCashCard(
    id = "",
    name = "",
    icon = CardIcon.Cash,
    iconBackground = CardColor.Blue,
    balance = emptyBalance
)

val emptyRecipientCard = UiRecipient(
    id = "",
    name = "",
    icon = CardIcon.Person,
    iconBackground = CardColor.Blue,
    number = ""
)