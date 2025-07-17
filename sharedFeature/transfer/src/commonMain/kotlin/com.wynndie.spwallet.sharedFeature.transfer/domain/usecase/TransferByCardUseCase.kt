package com.wynndie.spwallet.sharedFeature.transfer.domain.usecase

import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.EmptyOutcome
import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import com.wynndie.spwallet.sharedCore.domain.error.getOrElse
import com.wynndie.spwallet.sharedCore.domain.model.AuthedCard
import com.wynndie.spwallet.sharedFeature.transfer.domain.model.Transfer
import com.wynndie.spwallet.sharedFeature.transfer.domain.repository.TransferRepository

class TransferByCardUseCase(
    private val transferRepository: TransferRepository,
) {

    suspend operator fun invoke(
        card: AuthedCard,
        receiverCardNumber: String,
        amount: String,
        comment: String
    ): EmptyOutcome<DataError.Remote> {

        val transfer = Transfer(
            receiverCardNumber = receiverCardNumber,
            amount = amount.toLong(),
            comment = comment
        )

        val cardBalance = transferRepository.makeTransaction(card.authKey, transfer)
            .getOrElse { return Outcome.Error(it) }

//        transferRepository.insertAuthedCard(
//            card = card.copy(balance = cardBalance.value)
//        )

        return Outcome.Success(Unit)
    }
}