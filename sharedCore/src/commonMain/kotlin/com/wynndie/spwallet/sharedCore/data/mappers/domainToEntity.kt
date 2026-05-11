package com.wynndie.spwallet.sharedCore.data.mappers

import com.wynndie.spwallet.sharedCore.data.database.entities.AuthedCardEntity
import com.wynndie.spwallet.sharedCore.data.database.entities.AuthedUserEntity
import com.wynndie.spwallet.sharedCore.data.database.entities.CustomCardEntity
import com.wynndie.spwallet.sharedCore.data.database.entities.RecipientEntity
import com.wynndie.spwallet.sharedCore.data.database.entities.UnauthedCardEntity
import com.wynndie.spwallet.sharedCore.domain.models.AuthedUser
import com.wynndie.spwallet.sharedCore.domain.models.cards.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.CustomCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.RecipientCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.UnauthedCard

fun AuthedCard.toEntity(): AuthedCardEntity {
    return AuthedCardEntity(
        id = id,
        authKey = authKey,
        server = server.name,
        name = name,
        number = number,
        balance = balance,
        color = color.id,
        icon = icon.id
    )
}

fun AuthedUser.toEntity(): AuthedUserEntity {
    return AuthedUserEntity(
        id = id,
        server = server.name,
        name = name
    )
}

fun CustomCard.toEntity(): CustomCardEntity {
    return CustomCardEntity(
        id = if (id.isBlank()) 0 else id.toInt(),
        server = server.name,
        name = name,
        balance = balance,
        color = color.id,
        icon = icon.id
    )
}

fun RecipientCard.toEntity(): RecipientEntity {
    return RecipientEntity(
        id = if (id.isBlank()) 0 else id.toInt(),
        server = server.name,
        name = name,
        number = number,
        color = color.id,
        icon = icon.id
    )
}

fun UnauthedCard.toEntity(): UnauthedCardEntity {
    return UnauthedCardEntity(
        id = id,
        server = server.name,
        name = name,
        number = number,
        color = color.id,
        icon = icon.id
    )
}