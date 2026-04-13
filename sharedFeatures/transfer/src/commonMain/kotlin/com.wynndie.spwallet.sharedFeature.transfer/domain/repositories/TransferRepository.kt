package com.wynndie.spwallet.sharedFeature.transfer.domain.repositories

import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.outcome.Outcome

interface TransferRepository {

    suspend fun makeTransaction(
        authKey: String,
        receiver: String,
        amount: Long,
        comment: String
    ): Outcome<Long, Error.Network>
}