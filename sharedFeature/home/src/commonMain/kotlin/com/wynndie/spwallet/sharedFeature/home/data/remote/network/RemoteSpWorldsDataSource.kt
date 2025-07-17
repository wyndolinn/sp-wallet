package com.wynndie.spwallet.sharedFeature.home.data.remote.network

import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import com.wynndie.spwallet.sharedCore.data.remote.model.CardBalanceDto
import com.wynndie.spwallet.sharedFeature.home.data.remote.model.TransferDto
import com.wynndie.spwallet.sharedFeature.home.data.remote.model.UnauthedUserDto

interface RemoteSpWorldsDataSource {
    suspend fun getUnauthedUser(authKey: String): Outcome<UnauthedUserDto, DataError.Remote>

    suspend fun getCardBalance(authKey: String): Outcome<CardBalanceDto, DataError.Remote>

    suspend fun makeTransaction(
        authKey: String,
        transaction: TransferDto
    ): Outcome<CardBalanceDto, DataError.Remote>
}