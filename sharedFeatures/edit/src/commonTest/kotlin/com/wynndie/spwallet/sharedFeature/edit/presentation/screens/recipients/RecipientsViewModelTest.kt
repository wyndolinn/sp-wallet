package com.wynndie.spwallet.sharedFeature.edit.presentation.screens.recipients

import androidx.compose.ui.text.input.TextFieldValue
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.domain.helpers.emptyRecipientCard
import com.wynndie.spwallet.sharedCore.domain.models.Servers
import com.wynndie.spwallet.sharedCore.domain.models.cards.RecipientCard
import com.wynndie.spwallet.sharedCore.domain.repositories.PreferencesRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.RecipientRepository
import com.wynndie.spwallet.sharedCore.domain.validators.CardNameValidator
import com.wynndie.spwallet.sharedCore.domain.validators.CardNumberValidator
import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEventController
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.SnackbarController
import com.wynndie.spwallet.sharedCore.presentation.models.UiText.ResourceString
import com.wynndie.spwallet.sharedCore.recipient_deleted
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
class RecipientsViewModelTest {

    private val preferencesRepository = mock<PreferencesRepository>()
    private val recipientRepository = mock<RecipientRepository>()
    private val navEventController = NavEventController()
    private val snackbarController = SnackbarController()
    private val cardNameValidator = CardNameValidator()
    private val cardNumberValidator = CardNumberValidator()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `viewModel should expose recipients filtered by selected server on init`() = runTest {
        val recipients = listOf(
            emptyRecipientCard.copy(server = Servers.SP),
            emptyRecipientCard.copy(server = Servers.SP_MINI),
        )
        val viewModel = createViewModel(initialRecipients = recipients, initialServer = Servers.SP)

        assertThat(viewModel.state.value.recipients).isEqualTo(listOf(recipients[0]))
    }

    @Test
    fun `onSelectRecipient should update selected recipient and input fields`() = runTest {
        val recipient = emptyRecipientCard.copy(name = RECIPIENT_NAME, number = "12345")
        val viewModel = createViewModel()

        viewModel.onAction(RecipientsAction.SelectRecipient(recipient))

        assertThat(viewModel.state.value.selectedRecipient).isEqualTo(recipient)
        assertThat(viewModel.state.value.nameInputFieldState.value.text).isEqualTo(RECIPIENT_NAME)
        assertThat(viewModel.state.value.numberInputFieldState.value.text).isEqualTo("12345")
    }

    @Test
    fun `onChangeRecipientValue should filter and update recipient input field`() = runTest {
        val viewModel = createViewModel()

        viewModel.onAction(RecipientsAction.ChangeRecipientValue(TextFieldValue("new name")))

        assertThat(viewModel.state.value.recipientInputFieldState.value.text).isEqualTo("new name")
    }

    @Test
    fun `saveRecipient should insert recipient and close overlays`() = runTest {
        val recipient = emptyRecipientCard.copy(
            id = "1",
            name = "Test",
            number = "12345",
            server = Servers.SP
        )
        val viewModel = createViewModel(
            initialRecipients = listOf(recipient),
            initialServer = Servers.SP
        )

        everySuspend { recipientRepository.insertRecipient(any()) } returns Unit

        viewModel.onAction(RecipientsAction.SelectRecipient(recipient))
        viewModel.onAction(RecipientsAction.SaveRecipient)

        assertThat(viewModel.state.value.selectedRecipient).isEqualTo(null)
        assertThat(viewModel.state.value.isEditRecipientSheetOpen).isFalse()
        verifySuspend { recipientRepository.insertRecipient(recipient) }
    }

    @Test
    fun `deleteRecipient should delete recipient send snackbar and close overlays`() = runTest {
        val recipient = emptyRecipientCard.copy(
            id = "1",
            name = "Test"
        )
        val viewModel = createViewModel()

        everySuspend { recipientRepository.deleteRecipient(recipient) } returns Unit

        val snackbarEvent = async { snackbarController.overlay.first() }

        viewModel.onAction(RecipientsAction.DeleteRecipient(recipient))

        val expectedMessage = ResourceString(Res.string.recipient_deleted)
        assertThat(snackbarEvent.await().message).isEqualTo(expectedMessage)
        assertThat(viewModel.state.value.selectedRecipient).isEqualTo(null)
        assertThat(viewModel.state.value.isDeleteDialogOpen).isFalse()

        verifySuspend { recipientRepository.deleteRecipient(recipient) }
    }

    @Test
    fun `makeTransfer should navigate to transfer with recipient number`() = runTest {
        val recipient = emptyRecipientCard.copy(number = "12345")
        val viewModel = createViewModel()

        val navEvent = async { navEventController.navEvent.first() }

        viewModel.onAction(RecipientsAction.SelectRecipient(recipient))
        viewModel.onAction(RecipientsAction.MakeTransfer)

        assertThat(navEvent.await()).isEqualTo(RecipientsNavEvent.NavigateToTransfer("12345"))
        assertThat(viewModel.state.value.selectedRecipient).isEqualTo(null)
    }

    @Test
    fun `navigateBack should navigate back`() = runTest {
        val viewModel = createViewModel()
        val navEvent = async { navEventController.navEvent.first() }

        viewModel.onAction(RecipientsAction.NavigateBack)

        assertThat(navEvent.await()).isEqualTo(RecipientsNavEvent.NavigateBack)
    }

    @Test
    fun `toggleEditRecipientSheet should update sheet state`() = runTest {
        val viewModel = createViewModel()

        viewModel.onAction(RecipientsAction.ToggleEditRecipientSheet(true))
        assertThat(viewModel.state.value.isEditRecipientSheetOpen).isTrue()

        viewModel.onAction(RecipientsAction.ToggleEditRecipientSheet(false))
        assertThat(viewModel.state.value.isEditRecipientSheetOpen).isFalse()
    }

    @Test
    fun `toggleDeleteRecipientDialog should update dialog state`() = runTest {
        val viewModel = createViewModel()

        viewModel.onAction(RecipientsAction.ToggleDeleteRecipientDialog(true))
        assertThat(viewModel.state.value.isDeleteDialogOpen).isTrue()

        viewModel.onAction(RecipientsAction.ToggleDeleteRecipientDialog(false))
        assertThat(viewModel.state.value.isDeleteDialogOpen).isFalse()
    }

    private fun createViewModel(
        initialRecipients: List<RecipientCard> = emptyList(),
        initialServer: Servers = Servers.SP,
    ): RecipientsViewModel {
        every { recipientRepository.getRecipients() } returns flowOf(initialRecipients)
        every { preferencesRepository.getSelectedServer() } returns flowOf(initialServer)

        return RecipientsViewModel(
            preferencesRepository = preferencesRepository,
            recipientRepository = recipientRepository,
            navEventController = navEventController,
            snackbarController = snackbarController,
            cardNameValidator = cardNameValidator,
            cardNumberValidator = cardNumberValidator,
        )
    }

    private companion object {
        const val RECIPIENT_NAME = "recipientName"
    }
}
