package com.wynndie.spwallet.sharedCore.domain.repository

import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.EmptyOutcome
import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import com.wynndie.spwallet.sharedCore.domain.model.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.model.CardBalance
import com.wynndie.spwallet.sharedCore.domain.model.CashCard
import com.wynndie.spwallet.sharedCore.domain.model.UnauthedCard
import kotlinx.coroutines.flow.Flow

interface CardsRepository {

    suspend fun getCardBalance(authKey: String): Outcome<CardBalance, DataError.Remote>

    suspend fun insertCashCard(card: CashCard): EmptyOutcome<DataError.Local>
    fun getCashCards(): Flow<List<CashCard>>
    suspend fun deleteCashCard(card: CashCard)

    suspend fun insertAuthedCard(card: AuthedCard): EmptyOutcome<DataError.Local>
    fun getAuthedCards(): Flow<List<AuthedCard>>
    suspend fun deleteAuthedCard(card: AuthedCard)

    suspend fun insertUnauthedCard(card: UnauthedCard): EmptyOutcome<DataError.Local>
    fun getUnauthedCards(): Flow<List<UnauthedCard>>
    suspend fun deleteUnauthedCard(card: UnauthedCard)
}