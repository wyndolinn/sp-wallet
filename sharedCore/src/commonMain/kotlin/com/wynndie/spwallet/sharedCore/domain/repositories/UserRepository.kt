package com.wynndie.spwallet.sharedCore.domain.repositories

import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.EmptyOutcome
import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import com.wynndie.spwallet.sharedCore.domain.models.AuthedUser
import com.wynndie.spwallet.sharedCore.domain.models.Cardholder
import com.wynndie.spwallet.sharedCore.domain.models.SpServers
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getUnauthedUser(
        authKey: String,
        server: SpServers
    ): Outcome<Cardholder, DataError.Remote>

    suspend fun insertAuthedUser(user: AuthedUser): EmptyOutcome<DataError.Local>
    fun getAuthedUsers(): Flow<List<AuthedUser>>
    suspend fun deleteAuthedUser(user: AuthedUser)
}