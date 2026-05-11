package com.wynndie.spwallet.sharedCore.data.mappers

import com.wynndie.spwallet.sharedCore.data.database.entities.AuthedCardEntity
import com.wynndie.spwallet.sharedCore.data.database.entities.AuthedUserEntity
import com.wynndie.spwallet.sharedCore.data.database.entities.CustomCardEntity
import com.wynndie.spwallet.sharedCore.data.database.entities.RecipientEntity
import com.wynndie.spwallet.sharedCore.data.database.entities.UnauthedCardEntity
import com.wynndie.spwallet.sharedCore.domain.models.AuthedUser
import com.wynndie.spwallet.sharedCore.domain.models.SpServers
import com.wynndie.spwallet.sharedCore.domain.models.cards.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardIcons
import com.wynndie.spwallet.sharedCore.domain.models.cards.CustomCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.RecipientCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.UnauthedCard

fun AuthedCardEntity.toDomain(): AuthedCard {
    return AuthedCard(
        id = id,
        authKey = authKey,
        server = SpServers.valueOf(server),
        name = name,
        number = number,
        balance = balance,
        color = CardColors.of(color),
        icon = CardIcons.of(icon)
    )
}

fun AuthedUserEntity.toDomain(): AuthedUser {
    return AuthedUser(
        id = id,
        server = SpServers.valueOf(server),
        name = name
    )
}

fun CustomCardEntity.toDomain(): CustomCard {
    return CustomCard(
        id = id.toString(),
        server = SpServers.valueOf(server),
        name = name,
        balance = balance,
        color = CardColors.of(color),
        icon = CardIcons.of(icon)
    )
}

fun RecipientEntity.toDomain(): RecipientCard {
    return RecipientCard(
        id = id.toString(),
        server = SpServers.valueOf(server),
        name = name,
        number = number,
        color = CardColors.of(color),
        icon = CardIcons.of(icon)
    )
}

fun UnauthedCardEntity.toDomain(): UnauthedCard {
    return UnauthedCard(
        id = id,
        server = SpServers.valueOf(server),
        name = name,
        number = number,
        color = CardColors.of(color),
        icon = CardIcons.of(icon)
    )
}