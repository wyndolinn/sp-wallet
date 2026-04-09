package com.wynndie.spwallet.sharedCore.domain.constants

import com.wynndie.spwallet.sharedCore.domain.models.AuthedUser
import com.wynndie.spwallet.sharedCore.domain.models.SpServers
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardIcons
import com.wynndie.spwallet.sharedCore.domain.models.cards.CustomCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.RecipientCard

val emptyAuthedUser = AuthedUser(
    id = "",
    name = "",
    server = SpServers.SP
)

val emptyRecipientCard = RecipientCard(
    id = "",
    name = "",
    number = "",
    color = CardColors.BLUE,
    icon = CardIcons.PERSON,
    server = SpServers.SP
)

val emptyCustomCard = CustomCard(
    id = "",
    name = "",
    balance = 0L,
    color = CardColors.BLUE,
    icon = CardIcons.CASH,
    server = SpServers.SP
)