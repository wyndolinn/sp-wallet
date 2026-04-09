package com.wynndie.spwallet.sharedFeature.home.domain.useCases

import com.wynndie.spwallet.sharedCore.domain.models.cards.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.repositories.CardsRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.first

class DeleteAuthedCardUseCase(
    private val cardsRepository: CardsRepository,
    private val preferencesRepository: PreferencesRepository
) {

    suspend operator fun invoke(card: AuthedCard) {
        cardsRepository.deleteAuthedCard(card)

        val authedCards = cardsRepository.getAuthedCards().first()
        if (authedCards.isNotEmpty()) return

        val server = preferencesRepository.getSelectedSpServer().first()
        val unauthedCards = cardsRepository.getUnauthedCards().first()
        unauthedCards.forEach {
            if (it.server == server) cardsRepository.deleteUnauthedCard(it)
        }
    }
}