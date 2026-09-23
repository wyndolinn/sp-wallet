package com.wynndie.spwallet.sharedFeature.home.domain.useCases

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import com.wynndie.spwallet.sharedCore.data.repositories.fakes.FakeCardsRepository
import com.wynndie.spwallet.sharedCore.data.repositories.fakes.FakeUserRepository
import com.wynndie.spwallet.sharedCore.domain.helpers.emptyAuthedCard
import com.wynndie.spwallet.sharedCore.domain.helpers.emptyCardholder
import com.wynndie.spwallet.sharedCore.domain.helpers.emptyUnauthedCard
import com.wynndie.spwallet.sharedCore.domain.models.Cardholder
import com.wynndie.spwallet.sharedCore.domain.models.Servers
import com.wynndie.spwallet.sharedCore.domain.models.cards.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.outcome.Outcome
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class SyncWithRemoteUseCaseTest {

    private lateinit var userRepository: FakeUserRepository
    private lateinit var cardsRepository: FakeCardsRepository
    private lateinit var useCase: SyncWithRemoteUseCase

    @BeforeTest
    fun setUp() {
        userRepository = FakeUserRepository()
        cardsRepository = FakeCardsRepository()
        useCase = SyncWithRemoteUseCase(userRepository, cardsRepository)
    }

    @Test
    fun `invoke should return Success and update everything when requests succeed`() = runTest {
        val cards = createCards(1)
        cards.forEach { cardsRepository.insertAuthedCard(it) }

        val unauthedCard = emptyUnauthedCard.copy(
            id = "unauthedId",
            name = "Unauthed Card",
            number = "5678",
        )
        val cardholder = emptyCardholder.copy(
            id = "userId",
            username = "username",
            cards = listOf(unauthedCard),
        )

        userRepository.getUnauthedUserResult = { _, _ -> Outcome.Success(cardholder) }
        cardsRepository.getCardBalanceResult = { Outcome.Success(5000L) }

        val result = useCase()

        assertThat(result).isEqualTo(Outcome.Success(Unit))

        val authedCards = cardsRepository.getAuthedCards().first()
        assertThat(authedCards).containsExactly(cards[0].copy(balance = 5000L))

        val unauthedCards = cardsRepository.getUnauthedCards().first()
        assertThat(unauthedCards).containsExactly(unauthedCard)

        val authedUsers = userRepository.getAuthedUsers().first()
        assertThat(authedUsers).containsExactly(cardholder.toAuthedUser())
    }

    @Test
    fun `invoke should delete first and middle cards when they are unauthorized`() = runTest {
        val cards = createCards(3)
        cards.forEach { cardsRepository.insertAuthedCard(it) }

        val unauthorizedKeys = listOf(cards[0].authKey, cards[1].authKey)

        userRepository.getUnauthedUserResult = { authKey, _ ->
            if (authKey in unauthorizedKeys) {
                Outcome.Error(Error.Network.UNAUTHORIZED)
            } else Outcome.Error(Error.Network.SERVER_ERROR)
        }
        cardsRepository.getCardBalanceResult = { Outcome.Error(Error.Network.UNAUTHORIZED) }

        useCase()

        val remainingCards = cardsRepository.getAuthedCards().first()
        assertThat(remainingCards).containsExactly(cards[2])
    }

    @Test
    fun `invoke should delete middle and first cards when they are unauthorized`() = runTest {
        val cards = createCards(3)
        cards.forEach { cardsRepository.insertAuthedCard(it) }

        val unauthorizedKeys = listOf(cards[1].authKey, cards[0].authKey)

        userRepository.getUnauthedUserResult = { authKey, _ ->
            if (authKey in unauthorizedKeys) Outcome.Error(Error.Network.UNAUTHORIZED)
            else Outcome.Error(Error.Network.SERVER_ERROR)
        }
        cardsRepository.getCardBalanceResult = { Outcome.Error(Error.Network.UNAUTHORIZED) }

        useCase()

        val remainingCards = cardsRepository.getAuthedCards().first()
        assertThat(remainingCards).containsExactly(cards[2])
    }

    @Test
    fun `invoke should return Error when getUnauthedUser returns non-unauthorized error`() =
        runTest {
            val cards = createCards(1)
            cards.forEach { cardsRepository.insertAuthedCard(it) }

            userRepository.getUnauthedUserResult =
                { _, _ -> Outcome.Error(Error.Network.SERVER_ERROR) }

            val result = useCase()

            assertThat(result).isEqualTo(Outcome.Error(Error.Network.SERVER_ERROR))
        }

    @Test
    fun `invoke should return Error when getCardBalance returns non-unauthorized error`() =
        runTest {
            val cards = createCards(1)
            cards.forEach { cardsRepository.insertAuthedCard(it) }

            userRepository.getUnauthedUserResult = { _, _ ->
                Outcome.Success(Cardholder("userId", Servers.SP, "name", emptyList()))
            }
            cardsRepository.getCardBalanceResult = { Outcome.Error(Error.Network.SERVER_ERROR) }

            val result = useCase()

            assertThat(result).isEqualTo(Outcome.Error(Error.Network.SERVER_ERROR))
        }

    private fun createCards(count: Int): List<AuthedCard> {
        return (1..count).map { i ->
            emptyAuthedCard.copy(
                id = "id:$i",
                authKey = "key:$i",
                name = "Card $i",
                number = "123$i",
                balance = 1000L * i,
            )
        }
    }
}
