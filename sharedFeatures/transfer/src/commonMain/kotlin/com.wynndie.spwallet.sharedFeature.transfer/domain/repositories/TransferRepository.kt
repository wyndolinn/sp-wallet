package com.wynndie.spwallet.sharedFeature.transfer.domain.repositories

import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.outcome.Outcome
import com.wynndie.spwallet.sharedFeature.transfer.domain.models.Transfer

interface TransferRepository {

    suspend fun makeTransaction(
        authKey: String,
        transfer: Transfer
    ): Outcome<Long, Error.Network>
}