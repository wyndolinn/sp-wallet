package com.wynndie.spwallet.sharedCore.presentation.models

import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

@Serializable
sealed interface UiText {

    data class DynamicString(val value: String) : UiText
    class ResourceString(val id: StringResource, vararg formatArgs: Any) : UiText {
        val args: Array<out Any> = formatArgs

        override fun equals(other: Any?): Boolean {
            return (this === other) || ((other is ResourceString) && (id == other.id) && args.contentEquals(
                other.args
            ))
        }

        override fun hashCode(): Int {
            var result = id.hashCode()
            result = (31 * result) + args.contentHashCode()
            return result
        }

        override fun toString(): String {
            return "ResourceString(id=$id, args=${args.contentToString()})"
        }
    }

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is ResourceString -> stringResource(id, *getArgsCompose(args))
        }
    }

    suspend fun asStringAsync(): String {
        return when (this) {
            is DynamicString -> value
            is ResourceString -> runCatching {
                getString(
                    id,
                    *getArgsAsync(args)
                )
            }.getOrElse { id.key }
        }
    }


    @Composable
    private fun getArgsCompose(args: Array<out Any>): Array<String> {
        return args.map {
            when (it) {
                is StringResource -> stringResource(it)
                else -> it.toString()
            }
        }.toTypedArray()
    }

    private suspend fun getArgsAsync(args: Array<out Any>): Array<String> {
        return args.map {
            when (it) {
                is StringResource -> getString(it)
                else -> it.toString()
            }
        }.toTypedArray()
    }
}