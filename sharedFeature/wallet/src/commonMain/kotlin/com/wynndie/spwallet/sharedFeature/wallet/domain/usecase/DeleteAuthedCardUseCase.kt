package com.wynndie.spwallet.sharedFeature.wallet.domain.usecase

import com.wynndie.spwallet.sharedFeature.wallet.domain.model.card.AuthedCard
import com.wynndie.spwallet.sharedFeature.wallet.domain.repository.WalletRepository
import kotlinx.coroutines.flow.first

class DeleteAuthedCardUseCase(
    private val walletRepository: WalletRepository
) {

    suspend operator fun invoke(card: AuthedCard) {
        walletRepository.deleteAuthedCard(card)

        val authedCards = walletRepository.getAuthedCards().first()
        if (authedCards.isNotEmpty()) return

        val unauthedCards = walletRepository.getUnauthedCards().first()
        unauthedCards.forEach { walletRepository.deleteUnauthedCard(it) }
    }
}