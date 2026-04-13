package com.wynndie.spwallet.sharedCore.data.remote.network

import com.wynndie.spwallet.sharedCore.data.remote.SP_WORLDS_URL
import com.wynndie.spwallet.sharedCore.data.remote.dto.CardholderDto
import com.wynndie.spwallet.sharedCore.data.remote.safeCall
import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.outcome.Outcome
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

class KtorRemoteSpWorldsUserDataSource(
    private val httpClient: HttpClient
) : RemoteSpWorldsUserDataSource {

    override suspend fun getUnauthedUser(
        authKey: String
    ): Outcome<CardholderDto, Error.Network> {
        return safeCall<CardholderDto> {
            httpClient.get(urlString = "$SP_WORLDS_URL/accounts/me") {
                header(HttpHeaders.Authorization, authKey)
            }
        }
    }
}