package com.wynndie.spwallet.sharedCore.data.repositories

import com.wynndie.spwallet.sharedCore.data.database.WalletDatabase
import com.wynndie.spwallet.sharedCore.data.database.entities.AuthedUserEntity
import com.wynndie.spwallet.sharedCore.data.network.SP_WORLDS_URL
import com.wynndie.spwallet.sharedCore.data.network.dto.CardholderDto
import com.wynndie.spwallet.sharedCore.data.network.safeCall
import com.wynndie.spwallet.sharedCore.domain.models.AuthedUser
import com.wynndie.spwallet.sharedCore.domain.models.Cardholder
import com.wynndie.spwallet.sharedCore.domain.models.SpServers
import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.outcome.Outcome
import com.wynndie.spwallet.sharedCore.domain.outcome.map
import com.wynndie.spwallet.sharedCore.domain.repositories.UserRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(
    private val httpClient: HttpClient,
    private val database: WalletDatabase
) : UserRepository {

    override suspend fun getUnauthedUser(
        authKey: String,
        server: SpServers
    ): Outcome<Cardholder, Error.Network> {
        return safeCall<CardholderDto> {
            httpClient.get(urlString = "$SP_WORLDS_URL/accounts/me") {
                header(HttpHeaders.Authorization, authKey)
            }
        }.map { it.toDomain(server) }
    }

    override suspend fun insertAuthedUser(
        user: AuthedUser
    ) {
        database.userDao.insertAuthedUser(AuthedUserEntity.from(user))
    }

    override fun getAuthedUsers(): Flow<List<AuthedUser>> {
        return database.userDao
            .getAuthedUsers()
            .map { authedUserEntities ->
                authedUserEntities.map { it.toDomain() }
            }
    }

    override suspend fun deleteAuthedUser(user: AuthedUser) {
        database.userDao.deleteAuthedUser(AuthedUserEntity.from(user))
    }
}