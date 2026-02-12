package com.wynndie.spwallet.sharedCore.data.remote.network

import com.wynndie.spwallet.sharedCore.data.remote.dto.CardholderDto
import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.Outcome

interface RemoteSpWorldsUserDataSource {
    suspend fun getUnauthedUser(authKey: String): Outcome<CardholderDto, DataError.Remote>
}