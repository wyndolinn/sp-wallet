package com.wynndie.spwallet.sharedFeature.home.presentation.models

import com.wynndie.spwallet.sharedCore.domain.models.cards.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.CustomCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.UnauthedCard

data class HomeCardsData(
    val authedCards: List<AuthedCard>,
    val unauthedCards: List<UnauthedCard>,
    val customCards: List<CustomCard>
)
