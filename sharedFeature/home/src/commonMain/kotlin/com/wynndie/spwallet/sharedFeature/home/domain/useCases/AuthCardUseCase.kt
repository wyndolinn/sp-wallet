package com.wynndie.spwallet.sharedFeature.home.domain.useCases

import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.EmptyOutcome
import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import com.wynndie.spwallet.sharedCore.domain.error.getOrElse
import com.wynndie.spwallet.sharedCore.domain.repositories.CardsRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.UserRepository
import com.wynndie.spwallet.sharedFeature.home.domain.encoders.AuthKeyEncoder

class AuthCardUseCase(
    private val userRepository: UserRepository,
    private val cardsRepository: CardsRepository,
    private val authKeyEncoder: AuthKeyEncoder
) {

    suspend operator fun invoke(id: String, token: String): EmptyOutcome<DataError> {

        val authKey = authKeyEncoder.encode(id, token)

        val user = userRepository.getUnauthedUser(authKey)
            .getOrElse { return Outcome.Error(it) }

        val cardBalance = cardsRepository.getCardBalance(authKey)
            .getOrElse { return Outcome.Error(it) }

        // Так как для получения данных о пользователе нужна карта,
        // мы точно знаем, что список не может быть пустым
        val card = user.cards.find { it.id == id }!!
        cardsRepository.insertAuthedCard(
            card.asAuthedCard(authKey, cardBalance.value)
        )

        return Outcome.Success(Unit)
    }
}