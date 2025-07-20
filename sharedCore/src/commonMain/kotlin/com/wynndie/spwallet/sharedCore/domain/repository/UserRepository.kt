package com.wynndie.spwallet.sharedCore.domain.repository

import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.EmptyOutcome
import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import com.wynndie.spwallet.sharedCore.domain.model.AuthedUser
import com.wynndie.spwallet.sharedCore.domain.model.UnauthedUser
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getUnauthedUser(authKey: String): Outcome<UnauthedUser, DataError.Remote>

    suspend fun insertAuthedUser(user: AuthedUser): EmptyOutcome<DataError.Local>
    fun getAuthedUsers(): Flow<List<AuthedUser>>
    suspend fun deleteAuthedUser(user: AuthedUser)
}