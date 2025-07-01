package com.wynndie.spwallet.sharedFeature.wallet.domain.usecase

import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.EmptyOutcome
import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import com.wynndie.spwallet.sharedCore.domain.error.getOrElse
import com.wynndie.spwallet.sharedCore.domain.error.getOrThrow
import com.wynndie.spwallet.sharedCore.domain.error.onError
import com.wynndie.spwallet.sharedFeature.wallet.domain.model.Transfer
import com.wynndie.spwallet.sharedFeature.wallet.domain.model.card.AuthedCard
import com.wynndie.spwallet.sharedFeature.wallet.domain.repository.WalletRepository

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