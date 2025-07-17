package com.wynndie.spwallet.sharedFeature.home.domain.usecase

import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.EmptyOutcome
import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import com.wynndie.spwallet.sharedCore.domain.error.getOrElse
import com.wynndie.spwallet.sharedFeature.home.domain.model.Transfer
import com.wynndie.spwallet.sharedCore.domain.model.AuthedCard
import com.wynndie.spwallet.sharedFeature.home.domain.repository.WalletRepository

class TransferByCardUseCase(
    private val walletRepository: WalletRepository,
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

        val cardBalance = walletRepository.makeTransaction(card.authKey, transfer)
            .getOrElse { return Outcome.Error(it) }

        walletRepository.insertAuthedCard(
            card = card.copy(balance = cardBalance.value)
        )

        return Outcome.Success(Unit)
    }
}