package com.wynndie.spwallet.sharedFeature.transfer.domain.repositories

import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import com.wynndie.spwallet.sharedCore.domain.models.CardBalance
import com.wynndie.spwallet.sharedFeature.transfer.domain.models.Transfer

interface TransferRepository {

    suspend fun makeTransaction(
        authKey: String,
        transfer: Transfer
    ): Outcome<CardBalance, DataError.Remote>
}