package com.wynndie.spwallet.sharedFeature.transfer.domain.repository

import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import com.wynndie.spwallet.sharedCore.domain.model.CardBalance
import com.wynndie.spwallet.sharedFeature.transfer.domain.model.Transfer

interface TransferRepository {

    suspend fun makeTransaction(
        authKey: String,
        transfer: Transfer
    ): Outcome<CardBalance, DataError.Remote>
}