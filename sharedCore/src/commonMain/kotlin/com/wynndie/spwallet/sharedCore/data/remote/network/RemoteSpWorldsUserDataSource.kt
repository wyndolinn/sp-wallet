package com.wynndie.spwallet.sharedCore.data.remote.network

import com.wynndie.spwallet.sharedCore.data.remote.dto.CardholderDto
import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.outcome.Outcome

interface RemoteSpWorldsUserDataSource {
    suspend fun getUnauthedUser(authKey: String): Outcome<CardholderDto, Error.Network>
}