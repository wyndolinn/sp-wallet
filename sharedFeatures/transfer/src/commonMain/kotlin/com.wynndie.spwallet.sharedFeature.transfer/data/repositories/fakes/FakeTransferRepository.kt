package com.wynndie.spwallet.sharedFeature.transfer.data.repositories.fakes

import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.outcome.Outcome
import com.wynndie.spwallet.sharedFeature.transfer.domain.repositories.TransferRepository

class FakeTransferRepository : TransferRepository {
    var makeTransactionResult: (authKey: String, receiver: String, amount: Long, comment: String) -> Outcome<Long, Error.Network> =
        { _, _, _, _ -> Outcome.Error(Error.Network.SERVER_ERROR) }

    override suspend fun makeTransaction(
        authKey: String,
        receiver: String,
        amount: Long,
        comment: String,
    ): Outcome<Long, Error.Network> {
        return makeTransactionResult(authKey, receiver, amount, comment)
    }
}
