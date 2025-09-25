package com.wynndie.spwallet.sharedFeature.home.domain.usecase

import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.EmptyOutcome
import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import com.wynndie.spwallet.sharedCore.domain.error.getOrNull
import com.wynndie.spwallet.sharedCore.domain.error.onError
import com.wynndie.spwallet.sharedCore.domain.error.onSuccess
import com.wynndie.spwallet.sharedCore.domain.models.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.models.UnauthedUser
import com.wynndie.spwallet.sharedCore.domain.repositories.CardsRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.UserRepository
import kotlinx.coroutines.flow.first

class SyncWithRemoteUseCase(
    private val userRepository: UserRepository,
    private val cardsRepository: CardsRepository
) {

    suspend operator fun invoke(): EmptyOutcome<DataError> {
        var authedCards = cardsRepository.getAuthedCards().first()
        var unAuthedUser: UnauthedUser? = null

        authedCards.forEach { authedCard ->
            updateAuthedCard(authedCard)
                .onError { error -> return Outcome.Error(error) }
                .onSuccess { user -> if (user != null) unAuthedUser = user }
        }

        clearUserAndUnauthedCardsData()

        unAuthedUser?.let { user ->
            userRepository.insertAuthedUser(user.toAuthedUser())

            authedCards = cardsRepository.getAuthedCards().first()
            user.cards.forEach { card ->
                val isCardAuthed = authedCards.any { it.id == card.id }
                if (!isCardAuthed) cardsRepository.insertUnauthedCard(card)
            }
        } ?: return Outcome.Error(DataError.Remote.UNAUTHORIZED)

        return Outcome.Success(Unit)
    }


    private suspend fun clearUserAndUnauthedCardsData() {
        userRepository.getAuthedUsers().first().forEach { user ->
            userRepository.deleteAuthedUser(user)
        }

        cardsRepository.getUnauthedCards().first().forEach { card ->
            cardsRepository.deleteUnauthedCard(card)
        }
    }

    private suspend fun updateAuthedCard(
        authedCard: AuthedCard
    ): Outcome<UnauthedUser?, DataError.Remote> {

        val user = userRepository.getUnauthedUser(authedCard.authKey).onError {
            if (it != DataError.Remote.UNAUTHORIZED) return Outcome.Error(it)
            cardsRepository.deleteAuthedCard(authedCard)
        }.getOrNull() ?: return Outcome.Success(null)

        val cardBalance = cardsRepository.getCardBalance(authedCard.authKey).onError {
            if (it != DataError.Remote.UNAUTHORIZED) return Outcome.Error(it)
            cardsRepository.deleteAuthedCard(authedCard)
        }.getOrNull() ?: return Outcome.Success(null)

        cardsRepository.insertAuthedCard(
            authedCard.copy(balance = cardBalance.value)
        )

        return Outcome.Success(user)
    }
}