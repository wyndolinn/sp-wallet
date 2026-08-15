package com.wynndie.spwallet.sharedCore.presentation.extensions

import androidx.compose.ui.text.input.TextFieldValue
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.presentation.formatters.InputFieldState
import com.wynndie.spwallet.sharedCore.presentation.formatters.asUiText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MutableStateFlowTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    data class TestState(
        val inputField: InputFieldState = InputFieldState(),
        val hasError: Boolean = false,
        val customValue: String = ""
    )

    @Test
    fun `resetValidationState should update state and return true when validation succeeds`() {
        val initialState = TestState(
            inputField = InputFieldState(
                value = TextFieldValue("valid"),
                hasError = true,
                supportingText = null
            )
        )
        val stateFlow = MutableStateFlow(initialState)

        val result = stateFlow.resetValidationState(
            inputField = { it.inputField },
            validator = { true to null },
            updateState = { copy(inputField = it) }
        )

        assertThat(result).isTrue()
        assertThat(stateFlow.value.inputField.hasError).isFalse()
        assertThat(stateFlow.value.inputField.supportingText).isEqualTo(null)
    }

    @Test
    fun `resetValidationState should not update state and return false when validation fails`() {
        val initialState = TestState(
            inputField = InputFieldState(value = TextFieldValue("invalid"), hasError = true)
        )
        val stateFlow = MutableStateFlow(initialState)

        val result = stateFlow.resetValidationState(
            inputField = { it.inputField },
            validator = { false to Error.Validation.INVALID_CHARACTERS },
            updateState = { copy(inputField = it) }
        )

        assertThat(result).isFalse()
        assertThat(stateFlow.value.inputField.hasError).isTrue()
    }

    @Test
    fun `validateInputField should set error and return false when text is not blank and validation fails`() {
        val initialState = TestState(
            inputField = InputFieldState(value = TextFieldValue("abc"))
        )
        val stateFlow = MutableStateFlow(initialState)

        val result = stateFlow.validateInputField(
            inputField = { it.inputField },
            validator = { false to Error.Validation.INVALID_CHARACTERS },
            updateState = { copy(inputField = it) }
        )

        assertThat(result).isFalse()
        assertThat(stateFlow.value.inputField.hasError).isTrue()
        assertThat(stateFlow.value.inputField.supportingText).isEqualTo(Error.Validation.INVALID_CHARACTERS.asUiText())
    }

    @Test
    fun `validateInputField should clear error and return true when text is valid`() {
        val initialState = TestState(
            inputField = InputFieldState(value = TextFieldValue("123"), hasError = true)
        )
        val stateFlow = MutableStateFlow(initialState)

        val result = stateFlow.validateInputField(
            inputField = { it.inputField },
            validator = { true to null },
            updateState = { copy(inputField = it) }
        )

        assertThat(result).isTrue()
        assertThat(stateFlow.value.inputField.hasError).isFalse()
        assertThat(stateFlow.value.inputField.supportingText).isEqualTo(null)
    }

    @Test
    fun `validateInputField should not set error when text is blank even if validation fails`() {
        val initialState = TestState(
            inputField = InputFieldState(value = TextFieldValue(""))
        )
        val stateFlow = MutableStateFlow(initialState)

        val result = stateFlow.validateInputField(
            inputField = { it.inputField },
            validator = { false to Error.Validation.EMPTY_FIELD },
            updateState = { copy(inputField = it) }
        )

        assertThat(result).isFalse()
        assertThat(stateFlow.value.inputField.hasError).isFalse()
        assertThat(stateFlow.value.inputField.supportingText).isEqualTo(null)
    }

    @Test
    fun `observeValidStates should update state and emit correct error state based on error flows`() =
        runTest {
            val stateFlow = MutableStateFlow(TestState())
            val errorFlow1 = MutableStateFlow(false)
            val errorFlow2 = MutableStateFlow(true)

            backgroundScope.launch(testDispatcher) {
                stateFlow.observeValidStates(errorFlow1, errorFlow2) {
                    copy(hasError = !it)
                }.collect()
            }

            assertThat(stateFlow.value.hasError).isTrue()

            errorFlow2.value = false
            assertThat(stateFlow.value.hasError).isFalse()
        }

    @Test
    fun `select should map state and emit distinct values only`() = runTest {
        val stateFlow = MutableStateFlow(TestState(customValue = "A"))

        val emissions = mutableListOf<String>()
        backgroundScope.launch(testDispatcher) {
            stateFlow.select { it.customValue }.collect {
                emissions.add(it)
            }
        }

        assertThat(emissions).isEqualTo(listOf("A"))

        stateFlow.value = stateFlow.value.copy(customValue = "A")
        assertThat(emissions).isEqualTo(listOf("A"))

        stateFlow.value = stateFlow.value.copy(customValue = "B")
        assertThat(emissions).isEqualTo(listOf("A", "B"))
    }
}
