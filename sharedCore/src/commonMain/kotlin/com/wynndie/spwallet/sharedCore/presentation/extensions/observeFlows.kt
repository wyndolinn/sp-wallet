package com.wynndie.spwallet.sharedCore.presentation.extensions

import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.presentation.states.InputFieldState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transform

fun <T> MutableStateFlow<T>.observeInputField(
    inputField: (T) -> InputFieldState,
    validation: (String) -> Pair<Boolean, Error.Validation?>,
    updateState: (InputFieldState) -> Unit
): Flow<Boolean> {
    return this
        .map { inputField(it) }
        .distinctUntilChanged()
        .onEach { inputField ->
            val (isValid, _) = validation(inputField.value.text)
            if (!isValid) return@onEach

            updateState(inputField.copy(supportingText = null, hasError = false))
        }
        .transform { inputField ->
            val (isValid, _) = validation(inputField.value.text)
            emit(isValid)
        }
}

fun <T> MutableStateFlow<T>.validateInputField(
    inputField: (T) -> InputFieldState,
    validation: (String) -> Pair<Boolean, Error.Validation?>,
    updateState: (InputFieldState) -> Unit
): Boolean {
    val field = inputField(this.value)

    val (isValid, error) = validation(field.value.text)
    val updatedField = field.copy(
        supportingText = if (field.value.text.isNotBlank()) error?.asUiText() else null,
        hasError = if (field.value.text.isNotBlank()) !isValid else false
    )

    updateState(updatedField)

    return isValid
}

fun observeValidationStates(
    vararg flows: Flow<Boolean>
): Flow<Boolean> {
    return combine(flows.toList()) { states ->
        states.all { it }
    }
}