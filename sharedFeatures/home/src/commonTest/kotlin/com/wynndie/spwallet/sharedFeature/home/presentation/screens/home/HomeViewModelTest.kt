package com.wynndie.spwallet.sharedFeature.home.presentation.screens.home

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.domain.helpers.emptyAuthedCard
import com.wynndie.spwallet.sharedCore.domain.helpers.emptyCardholder
import com.wynndie.spwallet.sharedCore.domain.models.AuthedUser
import com.wynndie.spwallet.sharedCore.domain.models.Servers
import com.wynndie.spwallet.sharedCore.domain.models.cards.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.CustomCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.UnauthedCard
import com.wynndie.spwallet.sharedCore.domain.outcome.Outcome
import com.wynndie.spwallet.sharedCore.domain.repositories.CardsRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.PreferencesRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.UserRepository
import com.wynndie.spwallet.sharedCore.not_enough_cards
import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEventController
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.SnackbarController
import com.wynndie.spwallet.sharedCore.presentation.formatters.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.models.UiText.ResourceString
import com.wynndie.spwallet.sharedCore.server_changed
import com.wynndie.spwallet.sharedFeature.home.domain.useCases.DeleteAuthedCardUseCase
import com.wynndie.spwallet.sharedFeature.home.domain.useCases.SyncWithRemoteUseCase
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val userRepository = mock<UserRepository>()
    private val cardsRepository = mock<CardsRepository>()
    private val preferencesRepository = mock<PreferencesRepository>()
    private val syncWithRemoteUseCase = SyncWithRemoteUseCase(userRepository, cardsRepository)
    private val deleteAuthedCardUseCase =
        DeleteAuthedCardUseCase(cardsRepository, userRepository, preferencesRepository)
    private val navEventController = NavEventController()
    private val snackbarController = SnackbarController()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `viewModel should initialize and sync with remote`() = runTest {
        every { cardsRepository.getAuthedCards() } returns flowOf(emptyList())
        every { userRepository.getAuthedUsers() } returns flowOf(emptyList())
        every { cardsRepository.getUnauthedCards() } returns flowOf(emptyList())
        every { cardsRepository.getCustomCards() } returns flowOf(emptyList())
        every { preferencesRepository.getSelectedServer() } returns flowOf(Servers.SP)
        everySuspend { userRepository.getUnauthedUser(any(), any()) } returns Outcome.Success(
            emptyCardholder
        )
        everySuspend { cardsRepository.getCardBalance(any()) } returns Outcome.Success(100L)
        everySuspend { cardsRepository.insertAuthedCard(any()) } returns Unit
        everySuspend { userRepository.insertAuthedUser(any()) } returns Unit

        val viewModel = createViewModel()

        assertThat(viewModel.state.value.screenLoadingState).isEqualTo(LoadingState.Finished)
    }

    @Test
    fun `selectServer should update server and send snackbar`() = runTest {
        every { cardsRepository.getAuthedCards() } returns flowOf(emptyList())
        every { userRepository.getAuthedUsers() } returns flowOf(emptyList())
        every { cardsRepository.getUnauthedCards() } returns flowOf(emptyList())
        every { cardsRepository.getCustomCards() } returns flowOf(emptyList())
        every { preferencesRepository.getSelectedServer() } returns flowOf(Servers.SP)
        everySuspend { preferencesRepository.setSelectedServer(Servers.SP_MINI) } returns Unit
        everySuspend { userRepository.getUnauthedUser(any(), any()) } returns Outcome.Success(
            emptyCardholder
        )
        everySuspend { cardsRepository.getCardBalance(any()) } returns Outcome.Success(100L)
        everySuspend { cardsRepository.insertAuthedCard(any()) } returns Unit
        everySuspend { userRepository.insertAuthedUser(any()) } returns Unit

        val snackbarEvent = async { snackbarController.overlay.first() }
        val viewModel = createViewModel()

        viewModel.onAction(HomeAction.SelectServer(Servers.SP_MINI))

        val expectedMessage = ResourceString(Res.string.server_changed, Servers.SP_MINI.label)
        assertThat(snackbarEvent.await().message).isEqualTo(expectedMessage)
        verifySuspend { preferencesRepository.setSelectedServer(Servers.SP_MINI) }
    }

    @Test
    fun `authCard should navigate to auth card screen`() = runTest {
        every { cardsRepository.getAuthedCards() } returns flowOf(emptyList())
        every { userRepository.getAuthedUsers() } returns flowOf(emptyList())
        every { cardsRepository.getUnauthedCards() } returns flowOf(emptyList())
        every { cardsRepository.getCustomCards() } returns flowOf(emptyList())
        every { preferencesRepository.getSelectedServer() } returns flowOf(Servers.SP)
        everySuspend { userRepository.getUnauthedUser(any(), any()) } returns Outcome.Success(
            emptyCardholder
        )
        everySuspend { cardsRepository.getCardBalance(any()) } returns Outcome.Success(100L)
        everySuspend { cardsRepository.insertAuthedCard(any()) } returns Unit
        everySuspend { userRepository.insertAuthedUser(any()) } returns Unit
        val viewModel = createViewModel()

        val navEvent = async { navEventController.navEvent.first() }

        viewModel.onAction(HomeAction.AuthCard)

        assertThat(navEvent.await()).isEqualTo(HomeNavEvent.NavigateToAuthCard)
    }

    @Test
    fun `transferBetweenCards should show not enough cards snackbar when cards count is 1 or less`() =
        runTest {
            val authedCards = listOf(emptyAuthedCard.copy(id = "1", server = Servers.SP))
            every { cardsRepository.getAuthedCards() } returns flowOf(authedCards)
            every { userRepository.getAuthedUsers() } returns flowOf(emptyList())
            every { cardsRepository.getUnauthedCards() } returns flowOf(emptyList())
            every { cardsRepository.getCustomCards() } returns flowOf(emptyList())
            every { preferencesRepository.getSelectedServer() } returns flowOf(Servers.SP)
            everySuspend { userRepository.getUnauthedUser(any(), any()) } returns Outcome.Success(
                emptyCardholder
            )
            everySuspend { cardsRepository.getCardBalance(any()) } returns Outcome.Success(100L)
            everySuspend { cardsRepository.insertAuthedCard(any()) } returns Unit
            everySuspend { userRepository.insertAuthedUser(any()) } returns Unit

            val viewModel = createViewModel(initialAuthedCards = authedCards)

            val snackbarEvent = async { snackbarController.overlay.first() }

            viewModel.onAction(HomeAction.TransferBetweenCards("1"))

            val expectedMessage = ResourceString(Res.string.not_enough_cards)
            assertThat(snackbarEvent.await().message).isEqualTo(expectedMessage)
        }

    @Test
    fun `transferBetweenCards should navigate when enough cards exist`() = runTest {
        val authedCards = listOf(
            emptyAuthedCard.copy(id = "1", server = Servers.SP),
            emptyAuthedCard.copy(id = "2", server = Servers.SP),
        )
        every { cardsRepository.getAuthedCards() } returns flowOf(authedCards)
        every { userRepository.getAuthedUsers() } returns flowOf(emptyList())
        every { cardsRepository.getUnauthedCards() } returns flowOf(emptyList())
        every { cardsRepository.getCustomCards() } returns flowOf(emptyList())
        every { preferencesRepository.getSelectedServer() } returns flowOf(Servers.SP)
        everySuspend { userRepository.getUnauthedUser(any(), any()) } returns Outcome.Success(
            emptyCardholder
        )
        everySuspend { cardsRepository.getCardBalance(any()) } returns Outcome.Success(100L)
        everySuspend { cardsRepository.insertAuthedCard(any()) } returns Unit
        everySuspend { userRepository.insertAuthedUser(any()) } returns Unit

        val viewModel = createViewModel(initialAuthedCards = authedCards)

        val navEvent = async { navEventController.navEvent.first() }

        viewModel.onAction(HomeAction.TransferBetweenCards("1"))

        assertThat(navEvent.await()).isEqualTo(HomeNavEvent.NavigateToTransferBetweenCards("1"))
    }

    @Test
    fun `deactivateCard should delete card and update state`() = runTest {
        val card = emptyAuthedCard.copy(id = "1")
        everySuspend { cardsRepository.deleteAuthedCard(card) } returns Unit
        every { cardsRepository.getAuthedCards() } returns flowOf(emptyList())
        every { userRepository.getAuthedUsers() } returns flowOf(emptyList())
        every { cardsRepository.getUnauthedCards() } returns flowOf(emptyList())
        every { cardsRepository.getCustomCards() } returns flowOf(emptyList())
        every { preferencesRepository.getSelectedServer() } returns flowOf(Servers.SP)
        everySuspend { userRepository.getUnauthedUser(any(), any()) } returns Outcome.Success(
            emptyCardholder
        )
        everySuspend { cardsRepository.getCardBalance(any()) } returns Outcome.Success(100L)
        everySuspend { cardsRepository.insertAuthedCard(any()) } returns Unit
        everySuspend { userRepository.insertAuthedUser(any()) } returns Unit

        val viewModel = createViewModel()

        viewModel.onAction(HomeAction.DeactivateCard(card))

        assertThat(viewModel.state.value.screenLoadingState).isEqualTo(LoadingState.Finished)
        verifySuspend { cardsRepository.deleteAuthedCard(card) }
    }

    @Test
    fun `editRecipients should navigate to recipients`() = runTest {
        every { cardsRepository.getAuthedCards() } returns flowOf(emptyList())
        every { userRepository.getAuthedUsers() } returns flowOf(emptyList())
        every { cardsRepository.getUnauthedCards() } returns flowOf(emptyList())
        every { cardsRepository.getCustomCards() } returns flowOf(emptyList())
        every { preferencesRepository.getSelectedServer() } returns flowOf(Servers.SP)
        everySuspend { userRepository.getUnauthedUser(any(), any()) } returns Outcome.Success(
            emptyCardholder
        )
        everySuspend { cardsRepository.getCardBalance(any()) } returns Outcome.Success(100L)
        everySuspend { cardsRepository.insertAuthedCard(any()) } returns Unit
        everySuspend { userRepository.insertAuthedUser(any()) } returns Unit
        val viewModel = createViewModel()

        val navEvent = async { navEventController.navEvent.first() }

        viewModel.onAction(HomeAction.EditRecipients)

        assertThat(navEvent.await()).isEqualTo(HomeNavEvent.NavigateToRecipients)
    }

    @Test
    fun `toggleAuthedCardSheet and toggleDeleteCardDialog should update overlay visibility`() =
        runTest {
            every { cardsRepository.getAuthedCards() } returns flowOf(emptyList())
            every { userRepository.getAuthedUsers() } returns flowOf(emptyList())
            every { cardsRepository.getUnauthedCards() } returns flowOf(emptyList())
            every { cardsRepository.getCustomCards() } returns flowOf(emptyList())
            every { preferencesRepository.getSelectedServer() } returns flowOf(Servers.SP)
            everySuspend { userRepository.deleteAuthedUser(any()) } returns Unit
            everySuspend { cardsRepository.deleteUnauthedCard(any()) } returns Unit
            everySuspend { userRepository.getUnauthedUser(any(), any()) } returns Outcome.Success(
                emptyCardholder
            )
            everySuspend { cardsRepository.getCardBalance(any()) } returns Outcome.Success(100L)
            everySuspend { cardsRepository.insertAuthedCard(any()) } returns Unit
            everySuspend { userRepository.insertAuthedUser(any()) } returns Unit
            val viewModel = createViewModel()

            viewModel.onAction(HomeAction.ToggleAuthedCardSheet(true))
            assertThat(viewModel.state.value.isAuthedCardSheetVisible).isTrue()

            viewModel.onAction(HomeAction.ToggleDeleteCardDialog(true))
            assertThat(viewModel.state.value.isDeactivateCardDialogVisible).isTrue()
            assertThat(viewModel.state.value.isAuthedCardSheetVisible).isTrue()

            viewModel.onAction(HomeAction.ToggleAuthedCardSheet(false))
            assertThat(viewModel.state.value.isAuthedCardSheetVisible).isFalse()
            assertThat(viewModel.state.value.isDeactivateCardDialogVisible).isFalse()
        }

    private fun createViewModel(
        initialAuthedCards: List<AuthedCard> = emptyList(),
        initialUnauthedCards: List<UnauthedCard> = emptyList(),
        initialCustomCards: List<CustomCard> = emptyList(),
        initialServer: Servers = Servers.SP,
    ): HomeViewModel {
        every { userRepository.getAuthedUsers() } returns flowOf(
            listOf(
                AuthedUser(
                    "id",
                    Servers.SP,
                    "name"
                )
            )
        )
        everySuspend { userRepository.deleteAuthedUser(any()) } returns Unit
        everySuspend { userRepository.insertAuthedUser(any()) } returns Unit
        everySuspend { userRepository.getUnauthedUser(any(), any()) } returns Outcome.Success(
            emptyCardholder
        )
        every { cardsRepository.getAuthedCards() } returns flowOf(initialAuthedCards)
        everySuspend { cardsRepository.deleteAuthedCard(any()) } returns Unit
        every { cardsRepository.getUnauthedCards() } returns flowOf(initialUnauthedCards)
        everySuspend { cardsRepository.deleteUnauthedCard(any()) } returns Unit
        every { cardsRepository.getCustomCards() } returns flowOf(initialCustomCards)
        every { preferencesRepository.getSelectedServer() } returns flowOf(initialServer)
        everySuspend { cardsRepository.getCardBalance(any()) } returns Outcome.Success(100L)
        everySuspend { cardsRepository.insertAuthedCard(any()) } returns Unit

        return HomeViewModel(
            userRepository = userRepository,
            cardsRepository = cardsRepository,
            preferencesRepository = preferencesRepository,
            syncWithRemoteUseCase = syncWithRemoteUseCase,
            deleteAuthedCardUseCase = deleteAuthedCardUseCase,
            navEventController = navEventController,
            snackbarController = snackbarController,
        )
    }
}
