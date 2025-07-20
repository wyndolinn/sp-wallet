package com.wynndie.spwallet.sharedCore.data.remote.network

import com.wynndie.spwallet.sharedCore.data.remote.model.CardBalanceDto
import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.Outcome

interface RemoteSpWorldsCardsDataSource {

    suspend fun getCardBalance(authKey: String): Outcome<CardBalanceDto, DataError.Remote>
}