package com.wynndie.spwallet.sharedCore.domain.constants

import com.wynndie.spwallet.sharedCore.domain.models.AuthedUser
import com.wynndie.spwallet.sharedCore.domain.models.cards.CustomCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.RecipientCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardIcons

val emptyAuthedUser = AuthedUser(
    id = "",
    name = ""
)

val emptyRecipientCard = RecipientCard(
    id = "",
    name = "",
    number = "",
    color = CardColors.BLUE,
    icon = CardIcons.PERSON,
)

val emptyCustomCard = CustomCard(
    id = "",
    name = "",
    balance = 0L,
    color = CardColors.BLUE,
    icon = CardIcons.CASH
)