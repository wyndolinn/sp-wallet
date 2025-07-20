package com.wynndie.spwallet.sharedCore.data.remote.network

import com.wynndie.spwallet.sharedCore.data.remote.model.UnauthedUserDto
import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.Outcome

interface RemoteSpWorldsUserDataSource {
    suspend fun getUnauthedUser(authKey: String): Outcome<UnauthedUserDto, DataError.Remote>
}