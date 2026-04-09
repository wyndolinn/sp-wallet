package com.wynndie.spwallet.sharedFeature.transfer.data.remote.network

import com.wynndie.spwallet.sharedCore.data.remote.dto.CardBalanceDto
import com.wynndie.spwallet.sharedCore.data.remote.safeCall
import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import com.wynndie.spwallet.sharedFeature.transfer.data.remote.dto.TransferDto
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders

class KtorRemoteSpWorldsTransferDataSource(
    private val httpClient: HttpClient
) : RemoteSpWorldsTransferDataSource {

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