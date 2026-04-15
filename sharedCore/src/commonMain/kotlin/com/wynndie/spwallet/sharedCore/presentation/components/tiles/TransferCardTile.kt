package com.wynndie.spwallet.sharedCore.presentation.components.tiles

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardIcons
import com.wynndie.spwallet.sharedCore.presentation.extensions.asColor
import com.wynndie.spwallet.sharedCore.presentation.extensions.asDisplayableOre
import com.wynndie.spwallet.sharedCore.presentation.extensions.asFormattedAmount
import com.wynndie.spwallet.sharedCore.presentation.extensions.asPainter
import com.wynndie.spwallet.sharedCore.presentation.extensions.cardColorGradient
import com.wynndie.spwallet.sharedCore.presentation.extensions.thenIfNotNull
import com.wynndie.spwallet.sharedCore.presentation.extensions.tileShadow
import com.wynndie.spwallet.sharedCore.presentation.theme.AppTheme
import com.wynndie.spwallet.sharedCore.presentation.theme.sizes
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.x_of_ore
import org.jetbrains.compose.resources.stringResource

@Composable
fun TransferCardTile(
    title: String,
    text: String,
    icon: Painter,
    color: Color,
    modifier: Modifier = Modifier,
    headline: String = "",
    onClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .tileShadow(MaterialTheme.shapes.large, color)
            .clip(MaterialTheme.shapes.medium)
            .cardColorGradient(color.copy(alpha = 0.05f))
            .thenIfNotNull(onClick) { Modifier.clickable(onClick = it) }
            .padding(MaterialTheme.spacing.medium)
    ) {
        if (headline.isNotBlank()) {
            Text(
                text = headline,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.offset(x = -(4.dp))
        ) {
            Image(
                painter = icon,
                contentDescription = null,
                colorFilter = ColorFilter.tint(color),
                modifier = Modifier.size(MaterialTheme.sizes.large)
            )

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = text,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TransferCardTilePreview() {
    AppTheme {
        val ore = 989898L.asDisplayableOre()
        TransferCardTile(
            headline = "Со счёта",
            title = stringResource(Res.string.x_of_ore, ore.value).uppercase().asFormattedAmount(),
            text = "12345 • pipipupu",
            icon = CardIcons.BANK_CARD.asPainter(),
            color = CardColors.GREEN.asColor(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium)
        )
    }
}