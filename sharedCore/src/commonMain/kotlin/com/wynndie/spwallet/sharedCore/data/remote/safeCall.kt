package com.wynndie.spwallet.sharedCore.data.remote

import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.outcome.Outcome
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): Outcome<T, Error.Network> {
    val response = try {
        execute()
    } catch (_: SocketTimeoutException) {
        return Outcome.Error(Error.Network.REQUEST_TIMEOUT)
    } catch (_: UnresolvedAddressException) {
        return Outcome.Error(Error.Network.NO_INTERNET)
    } catch (_: Exception) {
        currentCoroutineContext().ensureActive()
        return Outcome.Error(Error.Network.UNKNOWN)
    }
    return response.toOutcome<T>()
}

suspend inline fun <reified T> HttpResponse.toOutcome(): Outcome<T, Error.Network> {
    return when (this.status.value) {
        in 200..299 -> {
            try {
                Outcome.Success(this.body<T>())
            } catch (_: NoTransformationFoundException) {
                Outcome.Error(Error.Network.SERIALIZATION_ERROR)
            }
        }

        400 -> Outcome.Error(Error.Network.BAD_REQUEST)
        401 -> Outcome.Error(Error.Network.UNAUTHORIZED)
        403 -> Outcome.Error(Error.Network.FORBIDDEN)
        404 -> Outcome.Error(Error.Network.NOT_FOUND)
        408 -> Outcome.Error(Error.Network.REQUEST_TIMEOUT)
        429 -> Outcome.Error(Error.Network.TOO_MANY_REQUESTS)
        in 500..599 -> Outcome.Error(Error.Network.SERVER_ERROR)
        else -> Outcome.Error(Error.Network.UNKNOWN)
    }
}