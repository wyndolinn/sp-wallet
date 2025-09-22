package com.wynndie.spwallet.sharedCore.domain.constants

import com.wynndie.spwallet.sharedCore.domain.models.AuthedUser
import com.wynndie.spwallet.sharedCore.domain.models.CustomCard
import com.wynndie.spwallet.sharedCore.domain.models.RecipientCard
import com.wynndie.spwallet.sharedCore.presentation.models.cards.CardColor
import com.wynndie.spwallet.sharedCore.presentation.models.cards.CardIcon

val emptyAuthedUser = AuthedUser(
    id = "",
    name = ""
)

val emptyRecipientCard = RecipientCard(
    id = "",
    name = "",
    color = CardColor.Blue.ordinal,
    icon = CardIcon.Person.ordinal,
    cardNumber = ""
)

val emptyCustomCard = CustomCard(
    id = "",
    name = "",
    color = CardColor.Blue.ordinal,
    icon = CardIcon.Cash.ordinal,
    balance = 0L
)