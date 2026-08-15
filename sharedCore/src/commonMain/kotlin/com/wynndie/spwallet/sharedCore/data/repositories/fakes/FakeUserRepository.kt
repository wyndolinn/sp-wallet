package com.wynndie.spwallet.sharedCore.data.repositories.fakes

import com.wynndie.spwallet.sharedCore.domain.models.AuthedUser
import com.wynndie.spwallet.sharedCore.domain.models.Cardholder
import com.wynndie.spwallet.sharedCore.domain.models.Servers
import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.outcome.Outcome
import com.wynndie.spwallet.sharedCore.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update


class FakeUserRepository : UserRepository {

    private val authedUsers = MutableStateFlow<List<AuthedUser>>(emptyList())

    var getUnauthedUserResult: (authKey: String, server: Servers) -> Outcome<Cardholder, Error.Network> =
        { _, _ -> Outcome.Error(Error.Network.SERVER_ERROR) }


    fun setInitialAuthedUsers(users: List<AuthedUser>) = authedUsers.update { users }


    override suspend fun getUnauthedUser(
        authKey: String,
        server: Servers,
    ): Outcome<Cardholder, Error.Network> {
        return getUnauthedUserResult(authKey, server)
    }

    override suspend fun insertAuthedUser(user: AuthedUser) {
        authedUsers.update { list -> list.filterNot { it.id == user.id } + user }
    }

    override fun getAuthedUsers(): Flow<List<AuthedUser>> {
        return authedUsers
    }

    override suspend fun deleteAuthedUser(user: AuthedUser) {
        authedUsers.update { list -> list.filterNot { it.id == user.id } }
    }
}