package com.wynndie.spwallet.sharedCore.presentation.extensions

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.json.Json

inline fun <reified T> MutableStateFlow<T>.asSavedState(
    scope: CoroutineScope,
    savedStateHandle: SavedStateHandle,
    key: String
): MutableStateFlow<T> {

    val savedState = savedStateHandle.get<String>(key)?.let { json ->
        runCatching { Json.decodeFromString<T>(json) }.getOrNull()
    }

    if (savedState != null) this.value = savedState

    this.onEach { value ->
        savedStateHandle[key] = when (value) {
            is String, is Int, is Long, is Boolean, is Float, is Double -> value
            else -> Json.encodeToString(value)
        }
    }.launchIn(scope)

    return this
}