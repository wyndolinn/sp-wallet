package com.wynndie.spwallet.sharedFeature.transfer.data.repository

import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import com.wynndie.spwallet.sharedCore.domain.error.map
import com.wynndie.spwallet.sharedCore.domain.model.CardBalance
import com.wynndie.spwallet.sharedFeature.transfer.data.remote.model.TransferDto
import com.wynndie.spwallet.sharedFeature.transfer.data.remote.network.RemoteSpWorldsTransferDataSource
import com.wynndie.spwallet.sharedFeature.transfer.domain.model.Transfer
import com.wynndie.spwallet.sharedFeature.transfer.domain.repository.TransferRepository

class TransferRepositoryImpl(
    private val remoteSpWorldsTransferDataSource: RemoteSpWorldsTransferDataSource
) : TransferRepository {

    override suspend fun makeTransaction(
        authKey: String,
        transfer: Transfer
    ): Outcome<CardBalance, DataError.Remote> {
        return remoteSpWorldsTransferDataSource
            .makeTransaction(
                authKey = authKey,
                transaction = TransferDto.from(transfer)
            ).map { it.toCardBalance() }
    }
}