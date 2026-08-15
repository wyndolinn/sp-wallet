package com.wynndie.spwallet.sharedFeature.transfer.presentation.searchRecipient

import androidx.compose.ui.text.input.TextFieldValue
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.wynndie.spwallet.sharedCore.domain.helpers.emptyRecipientCard
import com.wynndie.spwallet.sharedCore.domain.models.Servers
import com.wynndie.spwallet.sharedCore.domain.models.cards.RecipientCard
import com.wynndie.spwallet.sharedCore.domain.repositories.PreferencesRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.RecipientRepository
import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEventController
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
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
class SearchRecipientViewModelTest {

    private val recipientRepository = mock<RecipientRepository>()
    private val preferencesRepository = mock<PreferencesRepository>()
    private val navEventController = NavEventController()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `viewModel should expose recipients filtered by server on init`() = runTest {
        val recipients = listOf(
            emptyRecipientCard.copy(id = "1", server = Servers.SP, name = "Alice"),
            emptyRecipientCard.copy(id = "2", server = Servers.SP_MINI, name = "Bob"),
        )
        val viewModel = createViewModel(initialRecipients = recipients, initialServer = Servers.SP)

        assertThat(viewModel.state.value.recipients).isEqualTo(listOf(recipients[0]))
    }

    @Test
    fun `changeRecipientValue with 5 digits and no results should set isNewRecipient to true`() =
        runTest {
            val viewModel = createViewModel(initialRecipients = emptyList())

            viewModel.onAction(SearchRecipientAction.ChangeRecipientValue(TextFieldValue("12345")))

            assertThat(viewModel.state.value.isNewRecipient).isTrue()
        }

    @Test
    fun `selectRecipient should navigate to transfer with card number`() = runTest {
        val viewModel = createViewModel()
        val navEvent = async { navEventController.navEvent.first() }

        viewModel.onAction(SearchRecipientAction.SelectRecipient("12345"))

        assertThat(navEvent.await()).isEqualTo(SearchRecipientNavEvent.NavigateToTransfer("12345"))
    }

    @Test
    fun `navigateBack should navigate back`() = runTest {
        val viewModel = createViewModel()
        val navEvent = async { navEventController.navEvent.first() }

        viewModel.onAction(SearchRecipientAction.NavigateBack)

        assertThat(navEvent.await()).isEqualTo(SearchRecipientNavEvent.NavigateBack)
    }

    private fun createViewModel(
        initialRecipients: List<RecipientCard> = emptyList(),
        initialServer: Servers = Servers.SP,
    ): SearchRecipientViewModel {
        every { recipientRepository.getRecipients() } returns flowOf(initialRecipients)
        every { preferencesRepository.getSelectedServer() } returns flowOf(initialServer)

        return SearchRecipientViewModel(
            recipientRepository = recipientRepository,
            preferencesRepository = preferencesRepository,
            navEventController = navEventController,
        )
    }
}
