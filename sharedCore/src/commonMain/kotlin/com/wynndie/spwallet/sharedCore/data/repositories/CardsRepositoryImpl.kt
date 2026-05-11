package com.wynndie.spwallet.sharedCore.data.repositories

import com.wynndie.spwallet.sharedCore.data.database.WalletDatabase
import com.wynndie.spwallet.sharedCore.data.mappers.toDomain
import com.wynndie.spwallet.sharedCore.data.mappers.toEntity
import com.wynndie.spwallet.sharedCore.data.network.SP_WORLDS_URL
import com.wynndie.spwallet.sharedCore.data.network.dto.CardBalanceDto
import com.wynndie.spwallet.sharedCore.data.network.safeCall
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
    private val database: WalletDatabase
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
        database.cardsDao.insertCustomCard(card.toEntity())
    }

    override fun getCustomCards(): Flow<List<CustomCard>> {
        return database.cardsDao.getCustomCards().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun deleteCustomCard(card: CustomCard) {
        database.cardsDao.deleteCustomCard(card.toEntity())
    }


    override suspend fun insertAuthedCard(
        card: AuthedCard
    ) {
        database.cardsDao.insertAuthedCard(card.toEntity())
    }

    override fun getAuthedCards(): Flow<List<AuthedCard>> {
        return database.cardsDao.getAuthedCards().map {
            entities -> entities.map { it.toDomain() }
        }
    }

    override suspend fun deleteAuthedCard(card: AuthedCard) {
        database.cardsDao.deleteAuthedCard(card.toEntity())
    }


    override suspend fun insertUnauthedCard(
        card: UnauthedCard
    ) {
        database.cardsDao.insertUnauthedCard(card.toEntity())
    }

    override fun getUnauthedCards(): Flow<List<UnauthedCard>> {
        return database.cardsDao.getUnauthedCards().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun deleteUnauthedCard(card: UnauthedCard) {
        database.cardsDao.deleteUnauthedCard(card.toEntity())
    }
}