package com.wynndie.spwallet.sharedFeature.home.domain.usecase

import com.wynndie.spwallet.sharedCore.domain.model.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.repository.CardsRepository
import kotlinx.coroutines.flow.first

class DeleteAuthedCardUseCase(
    private val cardsRepository: CardsRepository
) {

    suspend operator fun invoke(card: AuthedCard) {
        cardsRepository.deleteAuthedCard(card)

        val authedCards = cardsRepository.getAuthedCards().first()
        if (authedCards.isNotEmpty()) return

        val unauthedCards = cardsRepository.getUnauthedCards().first()
        unauthedCards.forEach { cardsRepository.deleteUnauthedCard(it) }
    }
}