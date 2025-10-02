package com.wynndie.spwallet.sharedCore.presentation.components.tiles.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardIcons
import com.wynndie.spwallet.sharedtheme.designSystem.tiles.horizontal.HorizontalTileMedium
import com.wynndie.spwallet.sharedCore.presentation.extensions.asColor
import com.wynndie.spwallet.sharedCore.presentation.extensions.asImage
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.activate_card
import com.wynndie.spwallet.sharedResources.require_activation
import com.wynndie.spwallet.sharedtheme.theme.AppTheme
import com.wynndie.spwallet.sharedtheme.theme.spacing
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun UnauthedCardTile(
    icon: Painter,
    iconBackground: Color,
    cardName: String,
    cardNumber: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    HorizontalTileMedium(
        leadingContent = {
            Image(
                painter = icon,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                contentDescription = null
            )
        },
        leadingContentShape = MaterialTheme.shapes.small,
        leadingContentBackground = iconBackground,
        title = "$cardNumber • $cardName",
        description = stringResource(Res.string.require_activation),
        onClick = onClick,
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainer)
    )
}

@Preview(showBackground = true)
@Composable
private fun UnauthedCardTilePreview() {
    AppTheme {
        UnauthedCardTile(
            icon = CardIcons.BANK_CARD.asImage(),
            iconBackground = CardColors.GREEN.asColor(),
            cardName = "pipipupu",
            cardNumber = "12345",
            modifier = Modifier.padding(MaterialTheme.spacing.medium)
        )
    }
}