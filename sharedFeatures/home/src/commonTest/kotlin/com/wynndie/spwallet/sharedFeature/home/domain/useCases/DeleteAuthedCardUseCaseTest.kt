package com.wynndie.spwallet.sharedFeature.home.domain.useCases

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import com.wynndie.spwallet.sharedCore.data.repositories.fakes.FakeCardsRepository
import com.wynndie.spwallet.sharedCore.data.repositories.fakes.FakePreferencesRepository
import com.wynndie.spwallet.sharedCore.data.repositories.fakes.FakeUserRepository
import com.wynndie.spwallet.sharedCore.domain.helpers.emptyAuthedCard
import com.wynndie.spwallet.sharedCore.domain.helpers.emptyUnauthedCard
import com.wynndie.spwallet.sharedCore.domain.models.AuthedUser
import com.wynndie.spwallet.sharedCore.domain.models.Servers
import com.wynndie.spwallet.sharedCore.domain.models.cards.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.UnauthedCard
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class DeleteAuthedCardUseCaseTest {

    private lateinit var cardsRepository: FakeCardsRepository
    private lateinit var userRepository: FakeUserRepository
    private lateinit var preferencesRepository: FakePreferencesRepository
    private lateinit var useCase: DeleteAuthedCardUseCase

    @BeforeTest
    fun setUp() {
        cardsRepository = FakeCardsRepository()
        userRepository = FakeUserRepository()
        preferencesRepository = FakePreferencesRepository()
        useCase = DeleteAuthedCardUseCase(cardsRepository, userRepository, preferencesRepository)
    }

    @Test
    fun `invoke should only delete specified card when other cards remain`() = runTest {
        val card1 = createAuthedCard("1")
        val card2 = createAuthedCard("2")
        cardsRepository.insertAuthedCard(card1)
        cardsRepository.insertAuthedCard(card2)

        useCase(card1)

        val authedCards = cardsRepository.getAuthedCards().first()
        assertThat(authedCards).containsExactly(card2)
        assertThat(userRepository.getAuthedUsers().first()).isEmpty()
        assertThat(cardsRepository.getUnauthedCards().first()).isEmpty()
    }

    @Test
    fun `invoke should delete server data when the last authed card is deleted`() = runTest {
        val card = createAuthedCard("1", server = Servers.SP)
        cardsRepository.insertAuthedCard(card)

        val unauthedCard = createUnauthedCard("unauthed", server = Servers.SP)
        cardsRepository.insertUnauthedCard(unauthedCard)
        val user = AuthedUser("id", Servers.SP, "name")
        userRepository.insertAuthedUser(user)

        preferencesRepository.setSelectedServer(Servers.SP)

        useCase(card)

        assertThat(cardsRepository.getAuthedCards().first()).isEmpty()
        assertThat(cardsRepository.getUnauthedCards().first()).isEmpty()
        assertThat(userRepository.getAuthedUsers().first()).isEmpty()
    }

    @Test
    fun `invoke should not delete data of other servers when last card is deleted`() = runTest {
        val card = createAuthedCard("1", server = Servers.SP)
        cardsRepository.insertAuthedCard(card)

        val otherServerUnauthedCard = createUnauthedCard("other", server = Servers.SP_MINI)
        cardsRepository.insertUnauthedCard(otherServerUnauthedCard)
        val otherServerUser = AuthedUser("otherId", Servers.SP_MINI, "otherName")
        userRepository.insertAuthedUser(otherServerUser)

        preferencesRepository.setSelectedServer(Servers.SP)

        useCase(card)

        assertThat(cardsRepository.getAuthedCards().first()).isEmpty()
        assertThat(cardsRepository.getUnauthedCards().first()).containsExactly(
            otherServerUnauthedCard,
        )
        assertThat(userRepository.getAuthedUsers().first()).containsExactly(otherServerUser)
    }

    private fun createAuthedCard(id: String, server: Servers = Servers.SP): AuthedCard {
        return emptyAuthedCard.copy(
            id = id,
            server = server,
            authKey = "key$id",
            name = "Card $id",
            number = "123$id",
        )
    }

    private fun createUnauthedCard(id: String, server: Servers = Servers.SP): UnauthedCard {
        return emptyUnauthedCard.copy(
            id = id,
            server = server,
            name = "Unauthed $id",
            number = "456$id",
        )
    }
}
