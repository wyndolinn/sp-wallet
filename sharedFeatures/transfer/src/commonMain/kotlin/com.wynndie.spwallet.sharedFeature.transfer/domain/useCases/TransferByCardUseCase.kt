package com.wynndie.spwallet.sharedFeature.transfer.domain.useCases

import com.wynndie.spwallet.sharedCore.domain.models.cards.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.outcome.EmptyOutcome
import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.outcome.Outcome
import com.wynndie.spwallet.sharedCore.domain.outcome.getOrElse
import com.wynndie.spwallet.sharedCore.domain.repositories.CardsRepository
import com.wynndie.spwallet.sharedFeature.transfer.domain.repositories.TransferRepository

class TransferByCardUseCase(
    private val transferRepository: TransferRepository,
    private val cardsRepository: CardsRepository
) {

    suspend operator fun invoke(
        card: AuthedCard,
        receiver: String,
        amount: String,
        comment: String
    ): EmptyOutcome<Error.Network> {
        
        val cardBalance = transferRepository.makeTransaction(
            authKey = card.authKey,
            receiver = receiver,
            amount = amount.toLong(),
            comment = comment
        ).getOrElse { return Outcome.Error(it) }

        cardsRepository.insertAuthedCard(card.copy(balance = cardBalance))

        return Outcome.Success(Unit)
    }
}