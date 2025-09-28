package com.wynndie.spwallet.sharedCore.domain.repositories

import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.EmptyOutcome
import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import com.wynndie.spwallet.sharedCore.domain.models.cards.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.CustomCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.UnauthedCard
import kotlinx.coroutines.flow.Flow

interface CardsRepository {

    suspend fun getCardBalance(authKey: String): Outcome<Long, DataError.Remote>

    suspend fun insertCustomCard(card: CustomCard): EmptyOutcome<DataError.Local>
    fun getCustomCards(): Flow<List<CustomCard>>
    suspend fun deleteCustomCard(card: CustomCard)

    suspend fun insertAuthedCard(card: AuthedCard): EmptyOutcome<DataError.Local>
    fun getAuthedCards(): Flow<List<AuthedCard>>
    suspend fun deleteAuthedCard(card: AuthedCard)

    suspend fun insertUnauthedCard(card: UnauthedCard): EmptyOutcome<DataError.Local>
    fun getUnauthedCards(): Flow<List<UnauthedCard>>
    suspend fun deleteUnauthedCard(card: UnauthedCard)
}