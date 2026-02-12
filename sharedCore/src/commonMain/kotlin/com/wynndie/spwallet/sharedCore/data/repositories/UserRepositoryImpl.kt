package com.wynndie.spwallet.sharedCore.data.repositories

import androidx.sqlite.SQLiteException
import com.wynndie.spwallet.sharedCore.data.local.dao.UserDao
import com.wynndie.spwallet.sharedCore.data.local.entities.AuthedUserEntity
import com.wynndie.spwallet.sharedCore.data.remote.network.RemoteSpWorldsUserDataSource
import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.EmptyOutcome
import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import com.wynndie.spwallet.sharedCore.domain.error.map
import com.wynndie.spwallet.sharedCore.domain.models.AuthedUser
import com.wynndie.spwallet.sharedCore.domain.models.Cardholder
import com.wynndie.spwallet.sharedCore.domain.models.SpServers
import com.wynndie.spwallet.sharedCore.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(
    private val remoteSpWorldsUserDataSource: RemoteSpWorldsUserDataSource,
    private val userDao: UserDao
) : UserRepository {

    override suspend fun getUnauthedUser(
        authKey: String,
        server: SpServers
    ): Outcome<Cardholder, DataError.Remote> {
        return remoteSpWorldsUserDataSource
            .getUnauthedUser(authKey = authKey)
            .map { it.toDomain(server) }
    }

    override suspend fun insertAuthedUser(
        user: AuthedUser
    ): EmptyOutcome<DataError.Local> {
        return try {
            userDao.insertAuthedUser(AuthedUserEntity.from(user))
            Outcome.Success(Unit)
        } catch (_: SQLiteException) {
            Outcome.Error(DataError.Local.DISK_FULL)
        }
    }

    override fun getAuthedUsers(): Flow<List<AuthedUser>> {
        return userDao
            .getAuthedUsers()
            .map { authedUserEntities ->
                authedUserEntities.map { it.toDomain() }
            }
    }

    override suspend fun deleteAuthedUser(user: AuthedUser) {
        userDao.deleteAuthedUser(AuthedUserEntity.from(user))
    }
}