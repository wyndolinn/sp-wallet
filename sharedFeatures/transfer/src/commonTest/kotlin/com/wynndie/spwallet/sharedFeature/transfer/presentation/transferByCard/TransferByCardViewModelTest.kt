package com.wynndie.spwallet.sharedFeature.transfer.presentation.transferByCard

import androidx.compose.ui.text.input.TextFieldValue
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.domain.helpers.emptyAuthedCard
import com.wynndie.spwallet.sharedCore.domain.models.AuthedUser
import com.wynndie.spwallet.sharedCore.domain.models.Servers
import com.wynndie.spwallet.sharedCore.domain.models.cards.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.outcome.Outcome
import com.wynndie.spwallet.sharedCore.domain.repositories.CardsRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.PreferencesRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.RecipientRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.UserRepository
import com.wynndie.spwallet.sharedCore.domain.validators.AmountValidator
import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEventController
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.SnackbarController
import com.wynndie.spwallet.sharedCore.presentation.formatters.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.models.UiText.ResourceString
import com.wynndie.spwallet.sharedCore.transaction_succeed
import com.wynndie.spwallet.sharedFeature.transfer.domain.repositories.TransferRepository
import com.wynndie.spwallet.sharedFeature.transfer.domain.useCases.TransferByCardUseCase
import com.wynndie.spwallet.sharedFeature.transfer.domain.validators.TransferCommentValidator
import dev.mokkery.answering.returns
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
class TransferByCardViewModelTest {

    private val userRepository = mock<UserRepository>()
    private val cardsRepository = mock<CardsRepository>()
    private val recipientRepository = mock<RecipientRepository>()
    private val preferencesRepository = mock<PreferencesRepository>()
    private val transferRepository = mock<TransferRepository>()
    private val transferByCardUseCase = TransferByCardUseCase(transferRepository, cardsRepository)
    private val amountValidator = AmountValidator()
    private val commentValidator = TransferCommentValidator()
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
    fun `viewModel should load user and source cards on init`() = runTest {
        val user = AuthedUser("id", Servers.SP, "John")
        val cards = listOf(emptyAuthedCard.copy(id = "1", server = Servers.SP))
        val viewModel = createViewModel(initialUser = user, initialCards = cards)

        assertThat(viewModel.state.value.user.name).isEqualTo("John")
        assertThat(viewModel.state.value.sourceCards).isEqualTo(cards)
        assertThat(viewModel.state.value.commentPrefix).isEqualTo("John: ")
    }

    @Test
    fun `selectSourceCard should update source card index`() = runTest {
        val cards = listOf(
            emptyAuthedCard.copy(id = "1", server = Servers.SP),
            emptyAuthedCard.copy(id = "2", server = Servers.SP),
        )
        val viewModel =
            createViewModel(initialCards = cards, args = TransferByCardParams("nonexistent"))

        viewModel.onAction(TransferByCardAction.SelectSourceCard(1))

        assertThat(viewModel.state.value.selectedSourceCardIndex).isEqualTo(1)
    }

    @Test
    fun `changeAmountValue and changeCommentValue should update input fields`() = runTest {
        val viewModel = createViewModel()

        viewModel.onAction(TransferByCardAction.ChangeAmountValue(TextFieldValue("500")))
        viewModel.onAction(TransferByCardAction.ChangeCommentValue(TextFieldValue("Hello")))

        assertThat(viewModel.state.value.amountInputFieldState.value.text).isEqualTo("500")
        assertThat(viewModel.state.value.commentInputFieldState.value.text).isEqualTo("Hello")
    }

    @Test
    fun `makeTransfer success should call transferRepository insert recipient send snackbar and navigate back`() =
        runTest {
            val cards =
                listOf(emptyAuthedCard.copy(id = "1", server = Servers.SP, authKey = "key1"))
            val viewModel = createViewModel(initialCards = cards, initialServer = Servers.SP)

            everySuspend {
                transferRepository.makeTransaction(
                    "key1",
                    "12345",
                    100L,
                    any()
                )
            } returns Outcome.Success(900L)
            everySuspend { cardsRepository.insertAuthedCard(any()) } returns Unit
            everySuspend { cardsRepository.getAuthedCards() } returns flowOf(cards)
            everySuspend { recipientRepository.insertRecipient(any()) } returns Unit

            val snackbarEvent = async { snackbarController.overlay.first() }
            val navEvent = async { navEventController.navEvent.first() }

            viewModel.updateRecipient("12345")
            viewModel.onAction(TransferByCardAction.ChangeAmountValue(TextFieldValue("100")))
            viewModel.onAction(TransferByCardAction.MakeTransfer)

            val expectedMessage = ResourceString(Res.string.transaction_succeed)
            assertThat(snackbarEvent.await().message).isEqualTo(expectedMessage)
            assertThat(navEvent.await()).isEqualTo(TransferByCardNavEvent.NavigateBack)
            assertThat(viewModel.state.value.loadingState).isEqualTo(LoadingState.Finished)

            verifySuspend { transferRepository.makeTransaction("key1", "12345", 100L, any()) }
            verifySuspend { recipientRepository.insertRecipient(any()) }
        }

    @Test
    fun `editRecipient should navigate to edit recipient`() = runTest {
        val viewModel = createViewModel()
        val navEvent = async { navEventController.navEvent.first() }

        viewModel.onAction(TransferByCardAction.EditRecipient)

        assertThat(navEvent.await()).isEqualTo(TransferByCardNavEvent.NavigateToEditRecipient)
    }

    @Test
    fun `navigateBack should navigate back`() = runTest {
        val viewModel = createViewModel()
        val navEvent = async { navEventController.navEvent.first() }

        viewModel.onAction(TransferByCardAction.NavigateBack)

        assertThat(navEvent.await()).isEqualTo(TransferByCardNavEvent.NavigateBack)
    }

    private fun createViewModel(
        initialUser: AuthedUser = AuthedUser("id", Servers.SP, "John"),
        initialCards: List<AuthedCard> = listOf(
            emptyAuthedCard.copy(
                id = "1",
                server = Servers.SP
            )
        ),
        initialServer: Servers = Servers.SP,
        args: TransferByCardParams = TransferByCardParams("1"),
    ): TransferByCardViewModel {
        everySuspend { userRepository.getAuthedUsers() } returns flowOf(listOf(initialUser))
        everySuspend { cardsRepository.getAuthedCards() } returns flowOf(initialCards)
        everySuspend { preferencesRepository.getSelectedServer() } returns flowOf(initialServer)

        return TransferByCardViewModel(
            args = args,
            userRepository = userRepository,
            cardsRepository = cardsRepository,
            recipientRepository = recipientRepository,
            preferencesRepository = preferencesRepository,
            transferByCardUseCase = transferByCardUseCase,
            amountValidator = amountValidator,
            commentValidator = commentValidator,
            navEventController = navEventController,
            snackbarController = snackbarController,
        )
    }
}
