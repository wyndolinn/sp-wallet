package com.wynndie.spwallet.sharedFeature.transfer.domain.useCases

import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.EmptyOutcome
import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import com.wynndie.spwallet.sharedCore.domain.error.getOrElse
import com.wynndie.spwallet.sharedCore.domain.models.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.repositories.CardsRepository
import com.wynndie.spwallet.sharedFeature.transfer.domain.models.Transfer
import com.wynndie.spwallet.sharedFeature.transfer.domain.repositories.TransferRepository

class TransferByCardUseCase(
    private val transferRepository: TransferRepository,
    private val cardsRepository: CardsRepository
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

        cardsRepository.insertAuthedCard(
            card = card.copy(balance = cardBalance.value)
        )

        return Outcome.Success(Unit)
    }
}