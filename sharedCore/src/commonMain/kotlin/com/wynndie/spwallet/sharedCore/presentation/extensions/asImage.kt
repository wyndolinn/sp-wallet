package com.wynndie.spwallet.sharedCore.presentation.extensions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.AddCard
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardIcons
import com.wynndie.spwallet.sharedCore.presentation.formatters.UiImage

@Composable
fun CardIcons.asImage(): Painter {
    return when (this) {
        CardIcons.CASH -> UiImage.VectorImage(Icons.Outlined.AccountBalanceWallet).asPainter()
        CardIcons.BANK_CARD -> UiImage.VectorImage(Icons.Outlined.CreditCard).asPainter()
        CardIcons.ADD_CARD -> UiImage.VectorImage(Icons.Outlined.AddCard).asPainter()
        CardIcons.PERSON -> UiImage.VectorImage(Icons.Outlined.Person).asPainter()
    }
}