package com.wynndie.spwallet.sharedFeature.transfer.data.remote.network

import com.wynndie.spwallet.sharedCore.data.remote.model.CardBalanceDto
import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import com.wynndie.spwallet.sharedFeature.transfer.data.remote.model.TransferDto

interface RemoteSpWorldsTransferDataSource {
    suspend fun makeTransaction(
        authKey: String,
        transaction: TransferDto
    ): Outcome<CardBalanceDto, DataError.Remote>
}