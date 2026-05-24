package com.wynndie.spwallet.sharedCore.presentation.extensions

import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.presentation.formatters.InputFieldState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

fun <T> MutableStateFlow<T>.observeInputField(
    inputField: (T) -> InputFieldState,
    validation: (String) -> Pair<Boolean, Error.Validation?> = { true to null },
    updateState: (InputFieldState) -> Unit
): Flow<Boolean> {
    return this
        .map { inputField(it) }
        .distinctUntilChanged()
        .onEach {
            val (isValid, _) = validation(it.value.text)
            if (!isValid) return@onEach
            updateState(it.copy(supportingText = null, hasError = false))
        }
        .map { validation(it.value.text).first }
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
    return combine(flows.toList()) { states -> states.all { it } }
}