package com.wynndie.spwallet.sharedFeature.home.domain.useCases

import com.wynndie.spwallet.sharedCore.domain.models.Cardholder
import com.wynndie.spwallet.sharedCore.domain.models.SpServers
import com.wynndie.spwallet.sharedCore.domain.models.cards.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.outcome.EmptyOutcome
import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.outcome.Outcome
import com.wynndie.spwallet.sharedCore.domain.outcome.getOrNull
import com.wynndie.spwallet.sharedCore.domain.outcome.onError
import com.wynndie.spwallet.sharedCore.domain.outcome.onSuccess
import com.wynndie.spwallet.sharedCore.domain.repositories.CardsRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.UserRepository
import kotlinx.coroutines.flow.first

class SyncWithRemoteUseCase(
    private val userRepository: UserRepository,
    private val cardsRepository: CardsRepository
) {

    suspend operator fun invoke(): EmptyOutcome<Error.Network> {
        SpServers.entries.forEachIndexed { index, server ->
            var authedCards = cardsRepository.getAuthedCards().first()
            var cardholder: Cardholder? = null
            authedCards
                .filter { it.server == server }
                .forEach { authedCard ->
                    updateAuthedCard(authedCard)
                        .onError { error ->
                            return Outcome.Error(error)
                        }
                        .onSuccess { user ->
                            if (user != null) cardholder = user
                        }
                }

            userRepository.getAuthedUsers().first()
                .filter { it.server == server }
                .forEach { user ->
                    userRepository.deleteAuthedUser(user)
                }

            cardsRepository.getUnauthedCards().first()
                .filter { it.server == server }
                .forEach { card ->
                    cardsRepository.deleteUnauthedCard(card)
                }

            if (cardholder == null && index == SpServers.entries.lastIndex) {
                return Outcome.Error(Error.Network.UNAUTHORIZED)
            }

            cardholder?.let { user ->
                userRepository.insertAuthedUser(user.toAuthedUser())

                authedCards = cardsRepository.getAuthedCards().first()
                user.cards.forEach { card ->
                    val isCardAuthed = authedCards.any { it.id == card.id }
                    if (!isCardAuthed) cardsRepository.insertUnauthedCard(card)
                }
            }
        }

        return Outcome.Success(Unit)
    }

    private suspend fun updateAuthedCard(
        authedCard: AuthedCard
    ): Outcome<Cardholder?, Error.Network> {

        val user = userRepository.getUnauthedUser(
            authKey = authedCard.authKey,
            server = authedCard.server
        ).onError {
            if (it != Error.Network.UNAUTHORIZED) return Outcome.Error(it)
            cardsRepository.deleteAuthedCard(authedCard)
        }.getOrNull() ?: return Outcome.Success(null)

        val cardBalance = cardsRepository.getCardBalance(authedCard.authKey).onError {
            if (it != Error.Network.UNAUTHORIZED) return Outcome.Error(it)
            cardsRepository.deleteAuthedCard(authedCard)
        }.getOrNull() ?: return Outcome.Success(null)

        cardsRepository.insertAuthedCard(authedCard.copy(balance = cardBalance))

        return Outcome.Success(user)
    }
}