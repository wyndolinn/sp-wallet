package com.wynndie.spwallet.sharedCore.data.remote.network

import com.wynndie.spwallet.sharedCore.data.remote.dto.CardBalanceDto
import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.outcome.Outcome

interface RemoteSpWorldsCardsDataSource {
    suspend fun getCardBalance(authKey: String): Outcome<CardBalanceDto, Error.Network>
}