package com.wynndie.spwallet.sharedFeature.wallet.domain.repository

import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.EmptyOutcome
import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import com.wynndie.spwallet.sharedFeature.wallet.domain.model.card.AuthedCard
import com.wynndie.spwallet.sharedFeature.wallet.domain.model.AuthedUser
import com.wynndie.spwallet.sharedFeature.wallet.domain.model.card.CashCard
import com.wynndie.spwallet.sharedFeature.wallet.domain.model.card.RecipientCard
import com.wynndie.spwallet.sharedFeature.wallet.domain.model.Transfer
import com.wynndie.spwallet.sharedFeature.wallet.domain.model.card.UnauthedCard
import com.wynndie.spwallet.sharedFeature.wallet.domain.model.UnauthedUser
import com.wynndie.spwallet.sharedFeature.wallet.domain.model.card.CardBalance
import kotlinx.coroutines.flow.Flow

interface WalletRepository {

    suspend fun getUnauthedUser(authKey: String): Outcome<UnauthedUser, DataError.Remote>
    suspend fun getCardBalance(authKey: String): Outcome<CardBalance, DataError.Remote>
    suspend fun makeTransaction(
        authKey: String,
        transfer: Transfer
    ): Outcome<CardBalance, DataError.Remote>

    suspend fun insertAuthedUser(user: AuthedUser): EmptyOutcome<DataError.Local>
    fun getAuthedUsers(): Flow<List<AuthedUser>>
    suspend fun deleteAuthedUser(user: AuthedUser)

    suspend fun insertCashCard(card: CashCard): EmptyOutcome<DataError.Local>
    fun getCashCards(): Flow<List<CashCard>>
    suspend fun deleteCashCard(card: CashCard)

    suspend fun insertAuthedCard(card: AuthedCard): EmptyOutcome<DataError.Local>
    fun getAuthedCards(): Flow<List<AuthedCard>>
    suspend fun deleteAuthedCard(card: AuthedCard)

    suspend fun insertUnauthedCard(card: UnauthedCard): EmptyOutcome<DataError.Local>
    fun getUnauthedCards(): Flow<List<UnauthedCard>>
    suspend fun deleteUnauthedCard(card: UnauthedCard)

    suspend fun insertRecipient(recipientCard: RecipientCard): EmptyOutcome<DataError.Local>
    fun getRecipients(): Flow<List<RecipientCard>>
    suspend fun deleteRecipient(recipientCard: RecipientCard)
}