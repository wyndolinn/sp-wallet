package com.wynndie.spwallet.sharedFeature.home.presentation.screens.home.models

import com.wynndie.spwallet.sharedCore.presentation.models.cards.AuthedCardUi
import com.wynndie.spwallet.sharedCore.presentation.models.cards.CustomCardUi
import com.wynndie.spwallet.sharedCore.presentation.models.cards.UnauthedCardUi

data class HomeCardsData(
    val authedCards: List<AuthedCardUi>,
    val unauthedCards: List<UnauthedCardUi>,
    val customCards: List<CustomCardUi>
)
