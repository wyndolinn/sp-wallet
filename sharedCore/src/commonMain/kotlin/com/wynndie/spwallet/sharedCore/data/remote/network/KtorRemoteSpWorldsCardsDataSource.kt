package com.wynndie.spwallet.sharedCore.data.remote.network

import com.wynndie.spwallet.sharedCore.data.remote.SP_WORLDS_URL
import com.wynndie.spwallet.sharedCore.data.remote.dto.CardBalanceDto
import com.wynndie.spwallet.sharedCore.data.remote.safeCall
import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.outcome.Outcome
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

class KtorRemoteSpWorldsCardsDataSource(
    private val httpClient: HttpClient
) : RemoteSpWorldsCardsDataSource {

    override suspend fun getCardBalance(
        authKey: String
    ): Outcome<CardBalanceDto, Error.Network> {
        return safeCall<CardBalanceDto> {
            httpClient.get(urlString = "$SP_WORLDS_URL/card") {
                header(HttpHeaders.Authorization, authKey)
            }
        }
    }
}