package com.wynndie.spwallet.sharedCore.presentation.components.tiles.cards

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardIcons
import com.wynndie.spwallet.sharedCore.presentation.components.tiles.FilledTile
import com.wynndie.spwallet.sharedCore.presentation.extensions.asColor
import com.wynndie.spwallet.sharedCore.presentation.extensions.asImage
import com.wynndie.spwallet.sharedCore.presentation.extensions.joinToUiText
import com.wynndie.spwallet.sharedCore.presentation.formatters.displayableValue.OreDisplayableValue
import com.wynndie.spwallet.sharedCore.presentation.formatters.formatAsAmount
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.x_of_ore
import com.wynndie.spwallet.sharedtheme.theme.AppTheme
import com.wynndie.spwallet.sharedtheme.theme.spacing
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AuthedCardTile(
    icon: Painter,
    iconBackground: Color,
    cardName: String,
    cardNumber: String,
    balance: OreDisplayableValue,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    FilledTile(
        icon = icon,
        iconBackground = iconBackground,
        label = "$cardName • $cardNumber",
        title = stringResource(
            Res.string.x_of_ore,
            balance.value.toString().formatAsAmount()
        ).uppercase(),
        description = balance.formatted.joinToUiText(" ").asString(),
        onClick = onClick,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun AuthedCardTilePreview() {
    AppTheme {
        AuthedCardTile(
            icon = CardIcons.BANK_CARD.asImage(),
            iconBackground = CardColors.GREEN.asColor(),
            cardName = "pipipupu",
            cardNumber = "12345",
            balance = OreDisplayableValue.of(555555),
            onClick = {},
            modifier = Modifier.padding(MaterialTheme.spacing.medium)
        )
    }
}