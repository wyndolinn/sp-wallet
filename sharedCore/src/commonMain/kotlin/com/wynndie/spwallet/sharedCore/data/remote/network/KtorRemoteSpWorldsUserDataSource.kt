package com.wynndie.spwallet.sharedCore.data.remote.network

import com.wynndie.spwallet.sharedCore.data.remote.model.UnauthedUserDto
import com.wynndie.spwallet.sharedCore.data.safeCall
import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

class KtorRemoteSpWorldsUserDataSource(
    private val httpClient: HttpClient
) : RemoteSpWorldsUserDataSource {

    override suspend fun getUnauthedUser(
        authKey: String
    ): Outcome<UnauthedUserDto, DataError.Remote> {
        return safeCall<UnauthedUserDto> {
            httpClient.get(urlString = "$BASE_URL/accounts/me") {
                header(HttpHeaders.Authorization, authKey)
            }
        }
    }

    companion object {
        private const val BASE_URL = "https://spworlds.ru/api/public"
    }
}