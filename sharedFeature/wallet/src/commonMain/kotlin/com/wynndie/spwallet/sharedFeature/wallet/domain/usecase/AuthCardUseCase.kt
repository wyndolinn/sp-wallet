package com.wynndie.spwallet.sharedFeature.wallet.domain.usecase

import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.EmptyOutcome
import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import com.wynndie.spwallet.sharedCore.domain.error.getOrThrow
import com.wynndie.spwallet.sharedCore.domain.error.onError
import com.wynndie.spwallet.sharedFeature.wallet.domain.encoder.AuthKeyEncoder
import com.wynndie.spwallet.sharedFeature.wallet.domain.repository.WalletRepository

class AuthCardUseCase(
    private val walletRepository: WalletRepository,
    private val authKeyEncoder: AuthKeyEncoder,
) {

    suspend operator fun invoke(id: String, token: String): EmptyOutcome<DataError> {

        val authKey = authKeyEncoder.encode(id, token)

        val user = walletRepository.getUnauthedUser(authKey)
            .onError { return Outcome.Error(it) }
            .getOrThrow()

        val cardBalance = walletRepository.getCardBalance(authKey)
            .onError { return Outcome.Error(it) }
            .getOrThrow()

        // Так как для получения данных о пользователе нужна карта,
        // мы точно знаем, что список не может быть пустым
        val card = user.cards.find { it.id == id }!!
        walletRepository.insertAuthedCard(
            card.asAuthedCard(authKey, cardBalance.value)
        )

        return Outcome.Success(Unit)
    }
}