package com.wynndie.spwallet.sharedCore.data.repositories

import com.wynndie.spwallet.sharedCore.data.local.dao.CardsDao
import com.wynndie.spwallet.sharedCore.data.local.entities.AuthedCardEntity
import com.wynndie.spwallet.sharedCore.data.local.entities.CustomCardEntity
import com.wynndie.spwallet.sharedCore.data.local.entities.UnauthedCardEntity
import com.wynndie.spwallet.sharedCore.data.remote.SP_WORLDS_URL
import com.wynndie.spwallet.sharedCore.data.remote.dto.CardBalanceDto
import com.wynndie.spwallet.sharedCore.data.remote.safeCall
import com.wynndie.spwallet.sharedCore.domain.models.cards.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.CustomCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.UnauthedCard
import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.outcome.Outcome
import com.wynndie.spwallet.sharedCore.domain.outcome.map
import com.wynndie.spwallet.sharedCore.domain.repositories.CardsRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CardsRepositoryImpl(
    private val httpClient: HttpClient,
    private val cardsDao: CardsDao
) : CardsRepository {

    override suspend fun getCardBalance(
        authKey: String
    ): Outcome<Long, Error.Network> {
        return safeCall<CardBalanceDto> {
            httpClient.get(urlString = "$SP_WORLDS_URL/card") {
                header(HttpHeaders.Authorization, authKey)
            }
        }.map { it.balance }
    }


    override suspend fun insertCustomCard(
        card: CustomCard
    ) {
        cardsDao.insertCustomCard(CustomCardEntity.of(card))
    }

    override fun getCustomCards(): Flow<List<CustomCard>> {
        return cardsDao
            .getCustomCards()
            .map { cashCardEntities ->
                cashCardEntities.map { it.toDomain() }
            }
    }

    override suspend fun deleteCustomCard(card: CustomCard) {
        cardsDao.deleteCustomCard(CustomCardEntity.of(card))
    }


    override suspend fun insertAuthedCard(
        card: AuthedCard
    ) {
        cardsDao.insertAuthedCard(AuthedCardEntity.of(card))
    }

    override fun getAuthedCards(): Flow<List<AuthedCard>> {
        return cardsDao
            .getAuthedCards()
            .map { authedCardEntities ->
                authedCardEntities.map { it.toDomain() }
            }
    }

    override suspend fun deleteAuthedCard(card: AuthedCard) {
        cardsDao.deleteAuthedCard(AuthedCardEntity.of(card))
    }


    override suspend fun insertUnauthedCard(
        card: UnauthedCard
    ) {
        cardsDao.insertUnauthedCard(UnauthedCardEntity.from(card))
    }

    override fun getUnauthedCards(): Flow<List<UnauthedCard>> {
        return cardsDao
            .getUnauthedCards()
            .map { unAuthedCardEntities ->
                unAuthedCardEntities.map { it.toDomain() }
            }
    }

    override suspend fun deleteUnauthedCard(card: UnauthedCard) {
        cardsDao.deleteUnauthedCard(UnauthedCardEntity.from(card))
    }
}