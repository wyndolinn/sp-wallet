package com.wynndie.spwallet.sharedCore.presentation.extensions

import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.validators.core.ValidationValues
import com.wynndie.spwallet.sharedCore.presentation.formatters.InputFieldState
import com.wynndie.spwallet.sharedCore.presentation.formatters.asUiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

/**
 * Снимает состояние ошибки с поля, если его текущее значение прошло валидацию.
 *
 * Если значение невалидно, состояние поля не изменяется.
 */
fun <T> MutableStateFlow<T>.resetValidationState(
    inputField: (T) -> InputFieldState,
    validator: (ValidationValues) -> Pair<Boolean, Error.Validation?> = { true to null },
    updateState: T.(InputFieldState) -> T,
): Boolean {
    val field = inputField(this.value)
    val (isValid, _) = validator(ValidationValues(field.value.text))
    if (!isValid) return false

    val newInputField = field.copy(supportingText = null, hasError = false)
    this.update { it.updateState(newInputField) }
    return true
}

/**
 * Валидирует значение поля и обновляет соответствующее состояние в `StateFlow`.
 *
 * При успешной валидации очищает `supportingText` и сбрасывает `hasError`.
 */
fun <T> MutableStateFlow<T>.validateInputField(
    inputField: (T) -> InputFieldState,
    validator: (ValidationValues) -> Pair<Boolean, Error.Validation?> = { true to null },
    updateState: T.(InputFieldState) -> T,
): Boolean {
    val field = inputField(this.value)
    val (isValid, error) = validator(ValidationValues(field.value.text))
    val newInputField = field.copy(
        supportingText = if (field.value.text.isNotBlank()) error?.asUiText() else null,
        hasError = field.value.text.isNotBlank() && !isValid
    )
    this.update { it.updateState(newInputField) }
    return isValid
}

/**
 * Объединяет состояния ошибок и эмитит `true`, когда ни одно из полей не содержит ошибку.
 */
fun <T> MutableStateFlow<T>.observeValidStates(
    vararg errorFlows: Flow<Boolean>,
    updateState: T.(Boolean) -> T,
): Flow<Boolean> {
    return combine(errorFlows.toList()) { errorStates ->
        errorStates.none { it }
    }.onEach { isAllValid ->
        this.update { it.updateState(isAllValid) }
    }
}

/**
 * Создаёт поток, содержащий только изменившиеся результаты преобразования.
 */
fun <T, R> MutableStateFlow<T>.select(transform: (T) -> R): Flow<R> {
    return this.map(transform).distinctUntilChanged()
}