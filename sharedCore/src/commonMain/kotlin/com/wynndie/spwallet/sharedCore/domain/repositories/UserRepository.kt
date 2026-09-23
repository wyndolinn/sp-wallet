package com.wynndie.spwallet.sharedCore.domain.repositories

import com.wynndie.spwallet.sharedCore.domain.models.AuthedUser
import com.wynndie.spwallet.sharedCore.domain.models.Cardholder
import com.wynndie.spwallet.sharedCore.domain.models.Servers
import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.outcome.Outcome
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getUnauthedUser(
        authKey: String,
        server: Servers,
    ): Outcome<Cardholder, Error.Network>

    suspend fun insertAuthedUser(user: AuthedUser)
    fun getAuthedUsers(): Flow<List<AuthedUser>>
    suspend fun deleteAuthedUser(user: AuthedUser)
}