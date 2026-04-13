package com.wynndie.spwallet.sharedFeature.transfer.data.remote.network

import com.wynndie.spwallet.sharedCore.data.remote.dto.CardBalanceDto
import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.outcome.Outcome
import com.wynndie.spwallet.sharedFeature.transfer.data.remote.dto.TransferDto

interface RemoteSpWorldsTransferDataSource {
    suspend fun makeTransaction(
        authKey: String,
        transaction: TransferDto
    ): Outcome<CardBalanceDto, Error.Network>
}