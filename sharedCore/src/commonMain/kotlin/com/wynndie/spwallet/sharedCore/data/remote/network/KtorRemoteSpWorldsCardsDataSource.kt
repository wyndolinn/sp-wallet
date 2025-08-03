package com.wynndie.spwallet.sharedCore.data.remote.network

import com.wynndie.spwallet.sharedCore.data.remote.model.CardBalanceDto
import com.wynndie.spwallet.sharedCore.data.safeCall
import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

class KtorRemoteSpWorldsCardsDataSource(
    private val httpClient: HttpClient
) : RemoteSpWorldsCardsDataSource {

    override suspend fun getCardBalance(
        authKey: String
    ): Outcome<CardBalanceDto, DataError.Remote> {
        return safeCall<CardBalanceDto> {
            httpClient.get(urlString = "$BASE_URL/card") {
                header(HttpHeaders.Authorization, authKey)
            }
        }
    }

    companion object {
        private const val BASE_URL = "https://spworlds.ru/api/public"
    }
}