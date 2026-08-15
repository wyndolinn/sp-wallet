package com.wynndie.spwallet.sharedFeature.transfer.presentation.transferBetweenCards

import androidx.compose.ui.text.input.TextFieldValue
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.domain.helpers.emptyAuthedCard
import com.wynndie.spwallet.sharedCore.domain.models.Servers
import com.wynndie.spwallet.sharedCore.domain.models.cards.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.UnauthedCard
import com.wynndie.spwallet.sharedCore.domain.outcome.Outcome
import com.wynndie.spwallet.sharedCore.domain.repositories.CardsRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.PreferencesRepository
import com.wynndie.spwallet.sharedCore.domain.validators.AmountValidator
import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEventController
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.SnackbarController
import com.wynndie.spwallet.sharedCore.presentation.formatters.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.models.UiText.ResourceString
import com.wynndie.spwallet.sharedCore.transaction_succeed
import com.wynndie.spwallet.sharedFeature.transfer.domain.repositories.TransferRepository
import com.wynndie.spwallet.sharedFeature.transfer.domain.useCases.TransferByCardUseCase
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
class TransferBetweenCardsViewModelTest {

    private val cardsRepository = mock<CardsRepository>()
    private val preferencesRepository = mock<PreferencesRepository>()
    private val transferRepository = mock<TransferRepository>()
    private val transferByCardUseCase = TransferByCardUseCase(transferRepository, cardsRepository)
    private val amountValidator = AmountValidator()
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
    fun `viewModel should load source and destination cards on init`() = runTest {
        val authedCards = listOf(
            emptyAuthedCard.copy(id = "1", server = Servers.SP, balance = 1000L),
            emptyAuthedCard.copy(id = "2", server = Servers.SP, balance = 500L),
        )
        val viewModel =
            createViewModel(initialAuthedCards = authedCards, args = TransferBetweenCardsParams(""))
        val state = viewModel.state.first { it.sourceCards.size == 2 }

        assertThat(state.sourceCards.size).isEqualTo(2)
        assertThat(state.destinationCards.size).isEqualTo(1)
    }

    @Test
    fun `selectSourceCard and selectDestinationCard should update indices`() = runTest {
        val authedCards = listOf(
            emptyAuthedCard.copy(id = "1", server = Servers.SP),
            emptyAuthedCard.copy(id = "2", server = Servers.SP),
        )
        val viewModel =
            createViewModel(initialAuthedCards = authedCards, args = TransferBetweenCardsParams(""))
        viewModel.state.first { it.sourceCards.size == 2 }

        viewModel.onAction(TransferBetweenCardsAction.SelectSourceCard(1))
        viewModel.onAction(TransferBetweenCardsAction.SelectDestinationCard(0))

        assertThat(viewModel.state.value.selectedSourceCard).isEqualTo(1)
        assertThat(viewModel.state.value.selectedDestinationCard).isEqualTo(0)
    }

    @Test
    fun `changeAmountValue should update amount input field`() = runTest {
        val viewModel = createViewModel(args = TransferBetweenCardsParams(""))
        viewModel.state.first { it.sourceCards.isNotEmpty() }

        viewModel.onAction(TransferBetweenCardsAction.ChangeAmountValue(TextFieldValue("150")))

        assertThat(viewModel.state.value.amountInputFieldState.value.text).isEqualTo("150")
    }

    @Test
    fun `makeTransfer success should call transferRepository send snackbar and navigate to result`() =
        runTest {
            val authedCards = listOf(
                emptyAuthedCard.copy(
                    id = "1",
                    server = Servers.SP,
                    number = "11111",
                    authKey = "key1"
                ),
                emptyAuthedCard.copy(id = "2", server = Servers.SP, number = "22222"),
            )
            val viewModel = createViewModel(
                initialAuthedCards = authedCards,
                args = TransferBetweenCardsParams("")
            )
            viewModel.state.first { it.sourceCards.size == 2 }

            everySuspend {
                transferRepository.makeTransaction(
                    "key1",
                    "22222",
                    100L,
                    any()
                )
            } returns Outcome.Success(900L)
            everySuspend { cardsRepository.insertAuthedCard(any()) } returns Unit
            every { cardsRepository.getAuthedCards() } returns flowOf(authedCards)

            val snackbarEvent = async { snackbarController.overlay.first() }
            val navEvent = async { navEventController.navEvent.first() }

            viewModel.onAction(TransferBetweenCardsAction.ChangeAmountValue(TextFieldValue("100")))
            viewModel.onAction(TransferBetweenCardsAction.MakeTransfer)

            val expectedMessage = ResourceString(Res.string.transaction_succeed)
            assertThat(snackbarEvent.await().message).isEqualTo(expectedMessage)
            assertThat(navEvent.await()).isEqualTo(TransferBetweenCardsNavEvent.NavigateToResult)
            assertThat(viewModel.state.value.loadingState).isEqualTo(LoadingState.Finished)

            verifySuspend { transferRepository.makeTransaction("key1", "22222", 100L, any()) }
        }

    @Test
    fun `navigateBack should navigate back`() = runTest {
        val viewModel = createViewModel(args = TransferBetweenCardsParams(""))
        viewModel.state.first { it.sourceCards.isNotEmpty() }

        val navEvent = async { navEventController.navEvent.first() }

        viewModel.onAction(TransferBetweenCardsAction.NavigateBack)

        assertThat(navEvent.await()).isEqualTo(TransferBetweenCardsNavEvent.NavigateBack)
    }

    private fun createViewModel(
        initialAuthedCards: List<AuthedCard> = listOf(
            emptyAuthedCard.copy(id = "1", server = Servers.SP),
            emptyAuthedCard.copy(id = "2", server = Servers.SP),
        ),
        initialUnauthedCards: List<UnauthedCard> = emptyList(),
        initialServer: Servers = Servers.SP,
        args: TransferBetweenCardsParams = TransferBetweenCardsParams(""),
    ): TransferBetweenCardsViewModel {
        every { cardsRepository.getAuthedCards() } returns flowOf(initialAuthedCards)
        every { cardsRepository.getUnauthedCards() } returns flowOf(initialUnauthedCards)
        every { preferencesRepository.getSelectedServer() } returns flowOf(initialServer)

        return TransferBetweenCardsViewModel(
            cardsRepository = cardsRepository,
            preferencesRepository = preferencesRepository,
            args = args,
            transferByCardUseCase = transferByCardUseCase,
            transferAmountValidator = amountValidator,
            navEventController = navEventController,
            snackbarController = snackbarController,
        )
    }
}
