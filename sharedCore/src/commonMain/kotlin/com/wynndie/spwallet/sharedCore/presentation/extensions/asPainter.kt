package com.wynndie.spwallet.sharedCore.presentation.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardIcons
import com.wynndie.spwallet.sharedCore.presentation.formatters.UiImage
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.ic_add_card
import com.wynndie.spwallet.sharedResources.ic_card
import com.wynndie.spwallet.sharedResources.ic_person
import com.wynndie.spwallet.sharedResources.ic_wallet

@Composable
fun CardIcons.asPainter(): Painter {
    return when (this) {
        CardIcons.CASH -> UiImage.ResourceImage(Res.drawable.ic_wallet).asPainter()
        CardIcons.BANK_CARD -> UiImage.ResourceImage(Res.drawable.ic_card).asPainter()
        CardIcons.ADD_CARD -> UiImage.ResourceImage(Res.drawable.ic_add_card).asPainter()
        CardIcons.PERSON -> UiImage.ResourceImage(Res.drawable.ic_person).asPainter()
    }
}