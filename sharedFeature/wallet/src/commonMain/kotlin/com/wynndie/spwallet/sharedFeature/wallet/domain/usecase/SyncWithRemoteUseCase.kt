package com.wynndie.spwallet.sharedFeature.wallet.domain.usecase

import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.EmptyOutcome
import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import com.wynndie.spwallet.sharedCore.domain.error.getOrThrow
import com.wynndie.spwallet.sharedCore.domain.error.onError
import com.wynndie.spwallet.sharedCore.domain.error.onSuccess
import com.wynndie.spwallet.sharedFeature.wallet.domain.model.UnauthedUser
import com.wynndie.spwallet.sharedFeature.wallet.domain.model.card.AuthedCard
import com.wynndie.spwallet.sharedFeature.wallet.domain.repository.WalletRepository
import kotlinx.coroutines.flow.first

class SyncWithRemoteUseCase(
    private val walletRepository: WalletRepository
) {

    suspend operator fun invoke(): EmptyOutcome<DataError> {
        var authedCards = walletRepository.getAuthedCards().first()
        var unAuthedUser: UnauthedUser? = null

        authedCards.forEach { authedCard ->
            updateAuthedCard(authedCard)
                .onError { error -> return Outcome.Error(error) }
                .onSuccess { user ->  unAuthedUser = user }
        }

        clearUserAndUnauthedCardsData()

        unAuthedUser?.let { user ->
            walletRepository.insertAuthedUser(user.toAuthedUser())

            authedCards = walletRepository.getAuthedCards().first()
            user.cards.forEach { card ->
                val isCardAuthed = authedCards.any { it.id == card.id }
                if (!isCardAuthed) walletRepository.insertUnauthedCard(card)
            }
        } ?: return Outcome.Error(DataError.Remote.UNAUTHORIZED)

        return Outcome.Success(Unit)
    }


    private suspend fun clearUserAndUnauthedCardsData() {
        walletRepository.getAuthedUsers().first().forEach { user ->
            walletRepository.deleteAuthedUser(user)
        }

        walletRepository.getUnauthedCards().first().forEach { card ->
            walletRepository.deleteUnauthedCard(card)
        }
    }

    private suspend fun updateAuthedCard(
        authedCard: AuthedCard
    ): Outcome<UnauthedUser, DataError.Remote> {

        val user = walletRepository.getUnauthedUser(authedCard.authKey)
            .onError { error ->
                if (error != DataError.Remote.UNAUTHORIZED) return Outcome.Error(error)
                walletRepository.deleteAuthedCard(authedCard)
            }
            .getOrThrow()

        walletRepository.getCardBalance(authedCard.authKey)
            .onError { error ->
                if (error != DataError.Remote.UNAUTHORIZED) return Outcome.Error(error)
                walletRepository.deleteAuthedCard(authedCard)
            }
            .onSuccess { cardBalance ->
                walletRepository.insertAuthedCard(
                    authedCard.copy(balance = cardBalance.value)
                )
            }

        return Outcome.Success(user)
    }
}