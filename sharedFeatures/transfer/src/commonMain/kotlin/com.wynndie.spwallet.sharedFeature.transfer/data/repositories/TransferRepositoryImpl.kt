package com.wynndie.spwallet.sharedFeature.transfer.data.repositories

import com.wynndie.spwallet.sharedCore.data.remote.SP_WORLDS_URL
import com.wynndie.spwallet.sharedCore.data.remote.dto.CardBalanceDto
import com.wynndie.spwallet.sharedCore.data.remote.safeCall
import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.outcome.Outcome
import com.wynndie.spwallet.sharedCore.domain.outcome.map
import com.wynndie.spwallet.sharedFeature.transfer.domain.repositories.TransferRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import kotlinx.serialization.Serializable

class TransferRepositoryImpl(
    private val httpClient: HttpClient
) : TransferRepository {

    override suspend fun makeTransaction(
        authKey: String,
        receiver: String,
        amount: Long,
        comment: String
    ): Outcome<Long, Error.Network> {

        val transfer = TransferDto(
            receiver = receiver,
            amount = amount,
            comment = comment
        )

        return safeCall<CardBalanceDto> {
            httpClient.post(urlString = "$SP_WORLDS_URL/transactions") {
                header(HttpHeaders.Authorization, authKey)
                setBody(transfer)
            }
        }.map { it.balance }
    }

    companion object {
        @Serializable
        private data class TransferDto(
            val receiver: String,
            val amount: Long,
            val comment: String
        )
    }
}