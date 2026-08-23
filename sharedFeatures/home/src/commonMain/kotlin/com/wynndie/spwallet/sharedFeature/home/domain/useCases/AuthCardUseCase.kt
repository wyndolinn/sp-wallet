package com.wynndie.spwallet.sharedFeature.home.domain.useCases

import com.wynndie.spwallet.sharedCore.domain.models.SpServers
import com.wynndie.spwallet.sharedCore.domain.outcome.EmptyOutcome
import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.outcome.Outcome
import com.wynndie.spwallet.sharedCore.domain.outcome.getOrElse
import com.wynndie.spwallet.sharedCore.domain.repositories.CardsRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.UserRepository
import com.wynndie.spwallet.sharedFeature.home.domain.encoders.AuthKeyEncoder

class AuthCardUseCase(
    private val userRepository: UserRepository,
    private val cardsRepository: CardsRepository,
    private val authKeyEncoder: AuthKeyEncoder
) {

    suspend operator fun invoke(
        server: SpServers,
        id: String,
        token: String
    ): EmptyOutcome<Error.Network> {
        val authKey = authKeyEncoder.encode(id, token)

        val user = userRepository.getUnauthedUser(
            authKey = authKey,
            server = server
        ).getOrElse { return Outcome.Error(it) }

        val cardBalance = cardsRepository.getCardBalance(authKey)
            .getOrElse { return Outcome.Error(it) }

        val card = user.cards.first { it.id == id }
        cardsRepository.insertAuthedCard(card.toAuthedCard(authKey, cardBalance))

        return Outcome.Success(Unit)
    }
}