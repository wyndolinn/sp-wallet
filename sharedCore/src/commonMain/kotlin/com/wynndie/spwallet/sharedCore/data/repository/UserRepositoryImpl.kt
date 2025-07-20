package com.wynndie.spwallet.sharedCore.data.repository

import androidx.sqlite.SQLiteException
import com.wynndie.spwallet.sharedCore.data.local.database.WalletDao
import com.wynndie.spwallet.sharedCore.data.local.model.AuthedUserEntity
import com.wynndie.spwallet.sharedCore.data.remote.network.RemoteSpWorldsUserDataSource
import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.EmptyOutcome
import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import com.wynndie.spwallet.sharedCore.domain.error.map
import com.wynndie.spwallet.sharedCore.domain.model.AuthedUser
import com.wynndie.spwallet.sharedCore.domain.model.UnauthedUser
import com.wynndie.spwallet.sharedCore.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(
    private val remoteSpWorldsUserDataSource: RemoteSpWorldsUserDataSource,
    private val walletDao: WalletDao
) : UserRepository {

    override suspend fun getUnauthedUser(
        authKey: String
    ): Outcome<UnauthedUser, DataError.Remote> {
        return remoteSpWorldsUserDataSource
            .getUnauthedUser(authKey = authKey)
            .map { it.toUnauthedUser() }
    }


    override suspend fun insertAuthedUser(
        user: AuthedUser
    ): EmptyOutcome<DataError.Local> {
        return try {
            walletDao.insertAuthedUser(AuthedUserEntity.from(user))
            Outcome.Success(Unit)
        } catch (_: SQLiteException) {
            Outcome.Error(DataError.Local.DISK_FULL)
        }
    }

    override fun getAuthedUsers(): Flow<List<AuthedUser>> {
        return walletDao
            .getAuthedUsers()
            .map { authedUserEntities ->
                authedUserEntities.map { it.toAuthedUser() }
            }
    }

    override suspend fun deleteAuthedUser(user: AuthedUser) {
        walletDao.deleteAuthedUser(AuthedUserEntity.from(user))
    }
}