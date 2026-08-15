package com.wynndie.spwallet.sharedCore.data.repositories.fakes

import com.wynndie.spwallet.sharedCore.domain.models.cards.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.CustomCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.UnauthedCard
import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.outcome.Outcome
import com.wynndie.spwallet.sharedCore.domain.repositories.CardsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeCardsRepository : CardsRepository {

    private val customCards = MutableStateFlow<List<CustomCard>>(emptyList())
    private val authedCards = MutableStateFlow<List<AuthedCard>>(emptyList())
    private val unauthedCards = MutableStateFlow<List<UnauthedCard>>(emptyList())

    var getCardBalanceResult: (authKey: String) -> Outcome<Long, Error.Network> =
        { _ -> Outcome.Error(Error.Network.SERVER_ERROR) }


    fun setInitialCustomCards(cards: List<CustomCard>) = customCards.update { cards }
    fun setInitialAuthedCards(cards: List<AuthedCard>) = authedCards.update { cards }
    fun setInitialUnauthedCards(cards: List<UnauthedCard>) = unauthedCards.update { cards }


    override suspend fun getCardBalance(authKey: String): Outcome<Long, Error.Network> {
        return getCardBalanceResult(authKey)
    }

    override suspend fun insertCustomCard(card: CustomCard) {
        customCards.update { list -> list.filterNot { it.id == card.id } + card }
    }

    override fun getCustomCards(): Flow<List<CustomCard>> {
        return customCards
    }

    override suspend fun deleteCustomCard(card: CustomCard) {
        customCards.update { list -> list.filterNot { it.id == card.id } }
    }

    override suspend fun insertAuthedCard(card: AuthedCard) {
        authedCards.update { list -> list.filterNot { it.id == card.id } + card }
    }

    override fun getAuthedCards(): Flow<List<AuthedCard>> {
        return authedCards
    }

    override suspend fun deleteAuthedCard(card: AuthedCard) {
        authedCards.update { list -> list.filterNot { it.id == card.id } }
    }

    override suspend fun insertUnauthedCard(card: UnauthedCard) {
        unauthedCards.update { list -> list.filterNot { it.id == card.id } + card }
    }

    override fun getUnauthedCards(): Flow<List<UnauthedCard>> {
        return unauthedCards
    }

    override suspend fun deleteUnauthedCard(card: UnauthedCard) {
        unauthedCards.update { list -> list.filterNot { it.id == card.id } }
    }
}
