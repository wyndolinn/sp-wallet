package com.wynndie.spwallet.sharedFeature.edit.presentation.screens.customCard

import androidx.compose.ui.text.input.TextFieldValue
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.cash_creation_succeed
import com.wynndie.spwallet.sharedCore.domain.helpers.emptyCustomCard
import com.wynndie.spwallet.sharedCore.domain.models.Servers
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.domain.models.cards.CustomCard
import com.wynndie.spwallet.sharedCore.domain.repositories.CardsRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.PreferencesRepository
import com.wynndie.spwallet.sharedCore.domain.validators.AmountValidator
import com.wynndie.spwallet.sharedCore.domain.validators.CardNameValidator
import com.wynndie.spwallet.sharedCore.no_name
import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEventController
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.SnackbarController
import com.wynndie.spwallet.sharedCore.presentation.models.UiText.ResourceString
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

/** @see CustomCardViewModel */
@OptIn(ExperimentalCoroutinesApi::class)
class CustomCardViewModelTest {

    private val cardsRepository = mock<CardsRepository>()
    private val preferencesRepository = mock<PreferencesRepository>()
    private val cardNameValidator = CardNameValidator()
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
    fun `viewModel should expose initial state on init`() = runTest {
        val viewModel = createViewModel(
            args = CustomCardParams("$CARD_ID:1"),
            initialServer = Servers.SP,
            initialCards = List(3) {
                emptyCustomCard.copy(
                    id = "$CARD_ID:$it",
                    name = "$CARD_NAME:$it",
                    balance = it.toLong(),
                    color = CardColors.of(it),
                    server = Servers.SP,
                )
            },
        )

        val result = viewModel.state.value
        val expectedCard = result.card.copy(
            id = "$CARD_ID:1",
            name = "$CARD_NAME:1",
            server = Servers.SP,
            balance = 1L,
            color = CardColors.of(1),
        )

        assertThat(result.card).isEqualTo(expectedCard)
        assertThat(result.selectedColorChip).isEqualTo(CardColors.of(1))
        assertThat(result.nameInputFieldState.value.text).isEqualTo("$CARD_NAME:1")
        assertThat(result.balanceInputFieldState.value.text).isEqualTo("1")
    }

    @Test
    fun `viewModel should expose new card on init`() = runTest {
        val viewModel = createViewModel(
            args = CustomCardParams("unexpectedId"),
            initialServer = Servers.SP_MINI,
            initialCards = List(3) {
                emptyCustomCard.copy(
                    id = "$CARD_ID:$it",
                    name = "$CARD_NAME:$it",
                    balance = it.toLong(),
                    color = CardColors.of(it),
                    server = Servers.SP,
                )
            },
        )

        val result = viewModel.state.value
        val expected = result.copy(
            card = emptyCustomCard.copy(
                server = Servers.SP_MINI,
            ),
        )
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `onSaveCard should save card`() = runTest {
        val viewModel = createViewModel()
        val card = emptyCustomCard.copy(name = CARD_NAME)

        everySuspend { cardsRepository.getCustomCards() } returns flowOf(emptyList())
        everySuspend { cardsRepository.insertCustomCard(any()) } returns Unit

        val snackbarEvent = async { snackbarController.overlay.first() }
        val navEvent = async { navEventController.navEvent.first() }

        viewModel.onAction(CustomCardAction.SaveCard(card))

        val expectedMessage = ResourceString(Res.string.cash_creation_succeed)
        assertThat(snackbarEvent.await().message).isEqualTo(expectedMessage)
        assertThat(navEvent.await()).isEqualTo(CustomCardNavEvent.NavigateBack)

        verifySuspend { cardsRepository.insertCustomCard(card) }
    }

    @Test
    fun `onSaveCard should replace blank card name`() = runTest {
        val viewModel = createViewModel()
        val card = emptyCustomCard.copy(name = "")

        everySuspend { cardsRepository.getCustomCards() } returns flowOf(emptyList())
        everySuspend { cardsRepository.insertCustomCard(any()) } returns Unit

        val snackbarEvent = async { snackbarController.overlay.first() }
        val navEvent = async { navEventController.navEvent.first() }

        viewModel.onAction(CustomCardAction.SaveCard(card))

        val expectedMessage = ResourceString(Res.string.cash_creation_succeed)
        assertThat(snackbarEvent.await().message).isEqualTo(expectedMessage)
        assertThat(navEvent.await()).isEqualTo(CustomCardNavEvent.NavigateBack)

        val expectedName = ResourceString(Res.string.no_name).asStringAsync()
        verifySuspend { cardsRepository.insertCustomCard(card.copy(name = expectedName)) }
    }

    @Test
    fun `onDeleteCard should delete card and navigate back`() = runTest {
        val viewModel = createViewModel()
        val card = emptyCustomCard.copy(id = CARD_ID)

        everySuspend { cardsRepository.deleteCustomCard(card) } returns Unit

        val navEvent = async { navEventController.navEvent.first() }

        viewModel.onAction(CustomCardAction.DeleteCard(card))

        assertThat(viewModel.state.value.isDeleteDialogVisible).isFalse()
        assertThat(navEvent.await()).isEqualTo(CustomCardNavEvent.NavigateBack)

        verifySuspend { cardsRepository.deleteCustomCard(card) }
    }

    @Test
    fun `onSelectColor should update color and close sheet`() = runTest {
        val viewModel = createViewModel()
        val colorIndex = 1

        viewModel.onAction(CustomCardAction.SelectColor(colorIndex))

        val expectedColor = CardColors.of(colorIndex)
        assertThat(viewModel.state.value.card.color).isEqualTo(expectedColor)
        assertThat(viewModel.state.value.selectedColorChip).isEqualTo(expectedColor)
        assertThat(viewModel.state.value.isCustomizationSheetVisible).isFalse()
    }

    @Test
    fun `onChangeNameValue should update card name and input state`() = runTest {
        val viewModel = createViewModel()
        val newName = "new name"

        viewModel.onAction(CustomCardAction.ChangeNameValue(TextFieldValue(newName)))

        assertThat(viewModel.state.value.card.name).isEqualTo(newName)
        assertThat(viewModel.state.value.nameInputFieldState.value.text).isEqualTo(newName)
    }

    @Test
    fun `onChangeBalanceValue should update card balance and input state`() = runTest {
        val viewModel = createViewModel()
        val newBalance = TextFieldValue("250")

        viewModel.onAction(CustomCardAction.ChangeBalanceValue(newBalance))

        assertThat(viewModel.state.value.card.balance).isEqualTo(250L)
        assertThat(viewModel.state.value.balanceInputFieldState.value.text).isEqualTo("250")
    }

    @Test
    fun `onClearNameFocus should validate name field`() = runTest {
        val viewModel = createViewModel()

        viewModel.onAction(CustomCardAction.ChangeNameValue(TextFieldValue("Valid Name")))
        viewModel.onAction(CustomCardAction.ClearNameFocus)

        assertThat(viewModel.state.value.nameInputFieldState.hasError).isFalse()
    }

    @Test
    fun `onClearBalanceFocus should validate balance field`() = runTest {
        val viewModel = createViewModel()

        viewModel.onAction(CustomCardAction.ChangeBalanceValue(TextFieldValue("100")))
        viewModel.onAction(CustomCardAction.ClearBalanceFocus)

        assertThat(viewModel.state.value.balanceInputFieldState.hasError).isFalse()
    }

    @Test
    fun `observeInputValidation should manage save button enabled state correctly`() = runTest {
        val viewModel = createViewModel()

        assertThat(viewModel.state.value.isSaveButtonEnabled).isTrue()

        viewModel.onAction(CustomCardAction.ChangeNameValue(TextFieldValue("new name")))
        viewModel.onAction(CustomCardAction.ClearNameFocus)
        assertThat(viewModel.state.value.isSaveButtonEnabled).isTrue()

        viewModel.onAction(CustomCardAction.ChangeBalanceValue(TextFieldValue("50")))
        viewModel.onAction(CustomCardAction.ClearBalanceFocus)
        assertThat(viewModel.state.value.isSaveButtonEnabled).isTrue()
    }

    @Test
    fun `onNavigateBack should navigate back`() = runTest {
        val viewModel = createViewModel()

        val navEvent = async { navEventController.navEvent.first() }

        viewModel.onAction(CustomCardAction.NavigateBack)

        assertThat(navEvent.await()).isEqualTo(CustomCardNavEvent.NavigateBack)
    }

    @Test
    fun `onToggleCustomizationSheet should update customization sheet visibility`() = runTest {
        val viewModel = createViewModel()

        viewModel.onAction(CustomCardAction.ToggleCustomizationSheet(open = true))
        assertThat(viewModel.state.value.isCustomizationSheetVisible).isTrue()

        viewModel.onAction(CustomCardAction.ToggleCustomizationSheet(open = false))
        assertThat(viewModel.state.value.isCustomizationSheetVisible).isFalse()
    }

    @Test
    fun `onToggleDeleteDialog should update delete dialog visibility`() = runTest {
        val viewModel = createViewModel()

        viewModel.onAction(CustomCardAction.ToggleDeleteDialog(open = true))
        assertThat(viewModel.state.value.isDeleteDialogVisible).isTrue()

        viewModel.onAction(CustomCardAction.ToggleDeleteDialog(open = false))
        assertThat(viewModel.state.value.isDeleteDialogVisible).isFalse()
    }


    private fun createViewModel(
        args: CustomCardParams = CustomCardParams(""),
        initialServer: Servers = Servers.SP,
        initialCards: List<CustomCard> = emptyList(),
    ): CustomCardViewModel {
        everySuspend { cardsRepository.getCustomCards() } returns flowOf(initialCards)
        everySuspend { preferencesRepository.getSelectedServer() } returns flowOf(initialServer)

        return CustomCardViewModel(
            args = args,
            cardsRepository = cardsRepository,
            preferencesRepository = preferencesRepository,
            cardNameValidator = cardNameValidator,
            amountValidator = amountValidator,
            navEventController = navEventController,
            snackbarController = snackbarController,
        )
    }

    private companion object {
        const val CARD_ID = "cardId"
        const val CARD_NAME = "cardName"
    }
}
