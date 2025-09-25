package com.wynndie.spwallet.sharedCore.domain.constants

import com.wynndie.spwallet.sharedCore.domain.models.AuthedUser
import com.wynndie.spwallet.sharedCore.domain.models.CustomCard
import com.wynndie.spwallet.sharedCore.domain.models.RecipientCard
import com.wynndie.spwallet.sharedCore.domain.models.CardColor
import com.wynndie.spwallet.sharedCore.domain.models.CardIcon

val emptyAuthedUser = AuthedUser(
    id = "",
    name = ""
)

val emptyRecipientCard = RecipientCard(
    id = "",
    name = "",
    color = CardColor.BLUE.ordinal,
    icon = CardIcon.PERSON.ordinal,
    cardNumber = ""
)

val emptyCustomCard = CustomCard(
    id = "",
    name = "",
    color = CardColor.BLUE.ordinal,
    icon = CardIcon.CASH.ordinal,
    balance = 0L
)