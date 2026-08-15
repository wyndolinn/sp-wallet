package com.wynndie.spwallet.sharedFeature.home.domain.useCases

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import com.wynndie.spwallet.sharedCore.data.repositories.fakes.FakeCardsRepository
import com.wynndie.spwallet.sharedCore.data.repositories.fakes.FakeUserRepository
import com.wynndie.spwallet.sharedCore.domain.helpers.emptyCardholder
import com.wynndie.spwallet.sharedCore.domain.helpers.emptyUnauthedCard
import com.wynndie.spwallet.sharedCore.domain.models.Servers
import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.outcome.Outcome
import com.wynndie.spwallet.sharedFeature.home.domain.encoders.AuthKeyEncoder
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class AuthCardUseCaseTest {

    private lateinit var userRepository: FakeUserRepository
    private lateinit var cardsRepository: FakeCardsRepository
    private val authKeyEncoder = mock<AuthKeyEncoder>()

    private lateinit var useCase: AuthCardUseCase

    private val unauthedCard = emptyUnauthedCard.copy(id = CARD_ID)
    private val cardholder = emptyCardholder.copy(cards = listOf(unauthedCard))

    @BeforeTest
    fun setUp() {
        userRepository = FakeUserRepository()
        cardsRepository = FakeCardsRepository()
        useCase = AuthCardUseCase(userRepository, cardsRepository, authKeyEncoder)
    }

    @Test
    fun `invoke should insert authed card when everything succeeds`() = runTest {
        everySuspend { authKeyEncoder.encode(CARD_ID, CARD_TOKEN) } returns AUTH_KEY
        userRepository.getUnauthedUserResult = { _, _ -> Outcome.Success(cardholder) }
        cardsRepository.getCardBalanceResult = { Outcome.Success(100) }

        val result = useCase(Servers.SP, CARD_ID, CARD_TOKEN)

        val expectedAuthedCard = unauthedCard.toAuthedCard(AUTH_KEY, 100)
        assertThat(result).isEqualTo(Outcome.Success(Unit))
        val authedCards = cardsRepository.getAuthedCards().first()
        assertThat(authedCards).isEqualTo(listOf(expectedAuthedCard))
    }

    @Test
    fun `invoke should return Error when getUnauthedUser fails`() = runTest {
        val error = Error.Network.UNKNOWN
        everySuspend { authKeyEncoder.encode(CARD_ID, CARD_TOKEN) } returns AUTH_KEY
        userRepository.getUnauthedUserResult = { _, _ -> Outcome.Error(error) }

        val result = useCase(Servers.SP, CARD_ID, CARD_TOKEN)

        assertThat(result).isEqualTo(Outcome.Error(error))
        assertThat(cardsRepository.getAuthedCards().first()).isEmpty()
    }

    @Test
    fun `invoke should return Error when getCardBalance fails`() = runTest {
        val error = Error.Network.UNKNOWN
        everySuspend { authKeyEncoder.encode(CARD_ID, CARD_TOKEN) } returns AUTH_KEY
        userRepository.getUnauthedUserResult = { _, _ -> Outcome.Success(cardholder) }
        cardsRepository.getCardBalanceResult = { Outcome.Error(error) }

        val result = useCase(Servers.SP, CARD_ID, CARD_TOKEN)

        assertThat(result).isEqualTo(Outcome.Error(error))
        assertThat(cardsRepository.getAuthedCards().first()).isEmpty()
    }

    private companion object {
        const val CARD_ID = "cardId"
        const val CARD_TOKEN = "cardToken"
        const val AUTH_KEY = "Bearer encoded"
    }
}
