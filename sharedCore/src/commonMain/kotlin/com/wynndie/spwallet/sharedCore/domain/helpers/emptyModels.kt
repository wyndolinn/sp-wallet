package com.wynndie.spwallet.sharedCore.domain.helpers

import com.wynndie.spwallet.sharedCore.domain.models.AuthedUser
import com.wynndie.spwallet.sharedCore.domain.models.Cardholder
import com.wynndie.spwallet.sharedCore.domain.models.Servers
import com.wynndie.spwallet.sharedCore.domain.models.cards.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardIcons
import com.wynndie.spwallet.sharedCore.domain.models.cards.CustomCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.RecipientCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.UnauthedCard

val emptyAuthedUser = AuthedUser(
    id = "",
    name = "",
    server = Servers.SP,
)

val emptyAuthedCard = AuthedCard(
    id = "",
    name = "",
    number = "",
    color = CardColors.BLUE,
    icon = CardIcons.PERSON,
    server = Servers.SP,
    authKey = "",
    balance = 0,
)

val emptyUnauthedCard = UnauthedCard(
    id = "",
    name = "",
    number = "",
    color = CardColors.BLUE,
    icon = CardIcons.ADD_CARD,
    server = Servers.SP,
)

val emptyRecipientCard = RecipientCard(
    id = "",
    name = "",
    number = "",
    color = CardColors.BLUE,
    icon = CardIcons.PERSON,
    server = Servers.SP,
)

val emptyCustomCard = CustomCard(
    id = "",
    name = "",
    balance = 0,
    color = CardColors.BLUE,
    icon = CardIcons.CASH,
    server = Servers.SP,
)

val emptyCardholder = Cardholder(
    id = "",
    server = Servers.SP,
    username = "",
    cards = emptyList(),
)