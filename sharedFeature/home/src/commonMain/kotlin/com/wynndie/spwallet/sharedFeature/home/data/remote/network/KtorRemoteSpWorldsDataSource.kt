package com.wynndie.spwallet.sharedFeature.home.data.remote.network

import com.wynndie.spwallet.sharedCore.data.safeCall
import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import com.wynndie.spwallet.sharedFeature.home.data.remote.model.CardBalanceDto
import com.wynndie.spwallet.sharedFeature.home.data.remote.model.TransferDto
import com.wynndie.spwallet.sharedFeature.home.data.remote.model.UnauthedUserDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders

class KtorRemoteSpWorldsDataSource(
    private val httpClient: HttpClient
) : RemoteSpWorldsDataSource {

    override suspend fun getUnauthedUser(
        authKey: String
    ): Outcome<UnauthedUserDto, DataError.Remote> {
        return safeCall<UnauthedUserDto> {
            httpClient.get(urlString = "$BASE_URL/accounts/me") {
                header(HttpHeaders.Authorization, authKey)
            }
        }
    }

    override suspend fun getCardBalance(
        authKey: String
    ): Outcome<CardBalanceDto, DataError.Remote> {
        return safeCall<CardBalanceDto> {
            httpClient.get(urlString = "$BASE_URL/card") {
                header(HttpHeaders.Authorization, authKey)
            }
        }
    }

    override suspend fun makeTransaction(
        authKey: String,
        transaction: TransferDto
    ): Outcome<CardBalanceDto, DataError.Remote> {
        return safeCall<CardBalanceDto> {
            httpClient.post(urlString = "$BASE_URL/transactions") {
                header(HttpHeaders.Authorization, authKey)
                setBody(transaction)
            }
        }
    }

    companion object {
        private const val BASE_URL = "https://spworlds.ru/api/public"
    }
}