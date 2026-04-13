package com.wynndie.spwallet.sharedCore.domain.repositories

import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.outcome.Outcome
import com.wynndie.spwallet.sharedCore.domain.models.AuthedUser
import com.wynndie.spwallet.sharedCore.domain.models.Cardholder
import com.wynndie.spwallet.sharedCore.domain.models.SpServers
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getUnauthedUser(
        authKey: String,
        server: SpServers
    ): Outcome<Cardholder, Error.Network>

    suspend fun insertAuthedUser(user: AuthedUser)
    fun getAuthedUsers(): Flow<List<AuthedUser>>
    suspend fun deleteAuthedUser(user: AuthedUser)
}