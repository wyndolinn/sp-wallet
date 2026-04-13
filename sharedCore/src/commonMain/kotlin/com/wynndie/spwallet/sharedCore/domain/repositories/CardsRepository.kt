package com.wynndie.spwallet.sharedCore.domain.repositories

import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.outcome.Outcome
import com.wynndie.spwallet.sharedCore.domain.models.cards.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.CustomCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.UnauthedCard
import kotlinx.coroutines.flow.Flow

interface CardsRepository {

    suspend fun getCardBalance(authKey: String): Outcome<Long, Error.Network>

    suspend fun insertCustomCard(card: CustomCard)
    fun getCustomCards(): Flow<List<CustomCard>>
    suspend fun deleteCustomCard(card: CustomCard)

    suspend fun insertAuthedCard(card: AuthedCard)
    fun getAuthedCards(): Flow<List<AuthedCard>>
    suspend fun deleteAuthedCard(card: AuthedCard)

    suspend fun insertUnauthedCard(card: UnauthedCard)
    fun getUnauthedCards(): Flow<List<UnauthedCard>>
    suspend fun deleteUnauthedCard(card: UnauthedCard)
}