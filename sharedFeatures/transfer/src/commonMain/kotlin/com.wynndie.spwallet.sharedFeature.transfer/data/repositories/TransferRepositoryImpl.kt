package com.wynndie.spwallet.sharedFeature.transfer.data.repositories

import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.outcome.Outcome
import com.wynndie.spwallet.sharedCore.domain.outcome.map
import com.wynndie.spwallet.sharedFeature.transfer.data.remote.dto.TransferDto
import com.wynndie.spwallet.sharedFeature.transfer.data.remote.network.RemoteSpWorldsTransferDataSource
import com.wynndie.spwallet.sharedFeature.transfer.domain.models.Transfer
import com.wynndie.spwallet.sharedFeature.transfer.domain.repositories.TransferRepository

class TransferRepositoryImpl(
    private val remoteSpWorldsTransferDataSource: RemoteSpWorldsTransferDataSource
) : TransferRepository {

    override suspend fun makeTransaction(
        authKey: String,
        transfer: Transfer
    ): Outcome<Long, Error.Network> {
        return remoteSpWorldsTransferDataSource
            .makeTransaction(
                authKey = authKey,
                transaction = TransferDto.from(transfer)
            ).map { it.balance }
    }
}