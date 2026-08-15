package com.wynndie.spwallet.sharedFeature.home.presentation.screens.auth

import androidx.compose.ui.text.input.TextFieldValue
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.cash_creation_succeed
import com.wynndie.spwallet.sharedCore.domain.helpers.emptyUnauthedCard
import com.wynndie.spwallet.sharedCore.domain.models.Servers
import com.wynndie.spwallet.sharedCore.domain.models.cards.UnauthedCard
import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.outcome.Outcome
import com.wynndie.spwallet.sharedCore.domain.repositories.CardsRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.PreferencesRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.UserRepository
import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEventController
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.SnackbarController
import com.wynndie.spwallet.sharedCore.presentation.formatters.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.formatters.asUiText
import com.wynndie.spwallet.sharedCore.presentation.models.UiText.ResourceString
import com.wynndie.spwallet.sharedFeature.home.domain.encoders.AuthKeyEncoder
import com.wynndie.spwallet.sharedFeature.home.domain.useCases.AuthCardUseCase
import com.wynndie.spwallet.sharedFeature.home.domain.useCases.SyncWithRemoteUseCase
import com.wynndie.spwallet.sharedFeature.home.domain.validators.TokenValidator
import com.wynndie.spwallet.sharedFeature.home.domain.validators.UuidValidator
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
class AuthViewModelTest {

    private val cardsRepository = mock<CardsRepository>()
    private val preferencesRepository = mock<PreferencesRepository>()
    private val userRepository = mock<UserRepository>()
    private val authKeyEncoder = mock<AuthKeyEncoder>()
    private val authCardUseCase = AuthCardUseCase(userRepository, cardsRepository, authKeyEncoder)
    private val syncWithRemoteUseCase = SyncWithRemoteUseCase(userRepository, cardsRepository)
    private val uuidValidator = UuidValidator()
    private val tokenValidator = TokenValidator()
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
    fun `viewModel should expose unauthed cards filtered by selected server on init`() = runTest {
        val cards = listOf(
            emptyUnauthedCard.copy(id = "1", server = Servers.SP),
            emptyUnauthedCard.copy(id = "2", server = Servers.SP_MINI),
        )
        val viewModel = createViewModel(initialCards = cards, initialServer = Servers.SP)

        assertThat(viewModel.state.value.cards).isEqualTo(listOf(cards[0]))
    }

    @Test
    fun `authCard success should call useCase sync clear inputs and send success snackbar`() =
        runTest {
            val viewModel = createViewModel(initialServer = Servers.SP)
            val cardId = "123e4567-e89b-12d3-a456-426614174000"
            val cardToken = "dGVzdHRva2Vu"
            val authKey = "Bearer key"
            val cardholder = com.wynndie.spwallet.sharedCore.domain.models.Cardholder(
                id = cardId,
                server = Servers.SP,
                username = "Name",
                cards = listOf(emptyUnauthedCard.copy(id = cardId, server = Servers.SP)),
            )

            everySuspend { authKeyEncoder.encode(cardId, cardToken) } returns authKey
            everySuspend {
                userRepository.getUnauthedUser(
                    authKey,
                    Servers.SP
                )
            } returns Outcome.Success(cardholder)
            everySuspend { cardsRepository.getCardBalance(authKey) } returns Outcome.Success(100L)
            everySuspend { cardsRepository.insertAuthedCard(any()) } returns Unit
            every { cardsRepository.getAuthedCards() } returns flowOf(emptyList())
            every { userRepository.getAuthedUsers() } returns flowOf(emptyList())
            every { cardsRepository.getUnauthedCards() } returns flowOf(emptyList())

            val snackbarEvent = async { snackbarController.overlay.first() }

            viewModel.onAction(AuthAction.AuthCard(cardId, cardToken))

            val expectedMessage = ResourceString(Res.string.cash_creation_succeed)
            assertThat(snackbarEvent.await().message).isEqualTo(expectedMessage)
            assertThat(viewModel.state.value.loadingState).isEqualTo(LoadingState.Finished)

            verifySuspend { authKeyEncoder.encode(cardId, cardToken) }
        }

    @Test
    fun `authCard error should send error snackbar and not sync`() = runTest {
        val viewModel = createViewModel(initialServer = Servers.SP)
        val error = Error.Network.UNKNOWN
        val cardId = "123e4567-e89b-12d3-a456-426614174000"
        val cardToken = "dGVzdHRva2Vu"
        val authKey = "Bearer key"

        everySuspend { authKeyEncoder.encode(cardId, cardToken) } returns authKey
        everySuspend { userRepository.getUnauthedUser(authKey, Servers.SP) } returns Outcome.Error(
            error
        )

        val snackbarEvent = async { snackbarController.overlay.first() }

        viewModel.onAction(AuthAction.AuthCard(cardId, cardToken))

        assertThat(snackbarEvent.await().message).isEqualTo(error.asUiText())
        assertThat(viewModel.state.value.loadingState).isEqualTo(LoadingState.Finished)
    }

    @Test
    fun `changeIdValue and changeTokenValue should update input states`() = runTest {
        val viewModel = createViewModel()

        viewModel.onAction(AuthAction.ChangeIdValue(TextFieldValue("12345")))
        viewModel.onAction(AuthAction.ChangeTokenValue(TextFieldValue("token")))

        assertThat(viewModel.state.value.idInputFieldState.value.text).isEqualTo("12345")
        assertThat(viewModel.state.value.tokenInputFieldState.value.text).isEqualTo("token")
    }

    @Test
    fun `toggleHelpSheet should update help sheet visibility`() = runTest {
        val viewModel = createViewModel()

        viewModel.onAction(AuthAction.ToggleHelpSheet(true))
        assertThat(viewModel.state.value.isHelpSheetOpen).isTrue()

        viewModel.onAction(AuthAction.ToggleHelpSheet(false))
        assertThat(viewModel.state.value.isHelpSheetOpen).isFalse()
    }

    @Test
    fun `navigateBack should navigate back`() = runTest {
        val viewModel = createViewModel()
        val navEvent = async { navEventController.navEvent.first() }

        viewModel.onAction(AuthAction.NavigateBack)

        assertThat(navEvent.await()).isEqualTo(AuthNavEvent.NavigateBack)
    }

    private fun createViewModel(
        initialCards: List<UnauthedCard> = emptyList(),
        initialServer: Servers = Servers.SP,
    ): AuthViewModel {
        every { cardsRepository.getUnauthedCards() } returns flowOf(initialCards)
        every { preferencesRepository.getSelectedServer() } returns flowOf(initialServer)

        return AuthViewModel(
            cardsRepository = cardsRepository,
            preferencesRepository = preferencesRepository,
            authCardUseCase = authCardUseCase,
            syncWithRemoteUseCase = syncWithRemoteUseCase,
            uuidValidator = uuidValidator,
            tokenValidator = tokenValidator,
            navEventController = navEventController,
            snackbarController = snackbarController,
        )
    }
}
