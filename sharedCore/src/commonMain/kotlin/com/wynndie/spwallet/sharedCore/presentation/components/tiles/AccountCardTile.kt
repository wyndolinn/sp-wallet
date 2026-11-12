package com.wynndie.spwallet.sharedCore.presentation.components.tiles

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardIcons
import com.wynndie.spwallet.sharedCore.presentation.components.ColorTriangle
import com.wynndie.spwallet.sharedCore.presentation.extensions.thenIfNotNull
import com.wynndie.spwallet.sharedCore.presentation.formatters.DisplayableOreValue
import com.wynndie.spwallet.sharedCore.presentation.formatters.asColor
import com.wynndie.spwallet.sharedCore.presentation.formatters.asFormattedAmount
import com.wynndie.spwallet.sharedCore.presentation.formatters.asPainter
import com.wynndie.spwallet.sharedCore.presentation.theme.AppTheme
import com.wynndie.spwallet.sharedCore.presentation.theme.sizes
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedCore.x_of_ore
import org.jetbrains.compose.resources.stringResource

@Composable
fun AccountCardTile(
    label: String,
    title: String,
    text: String,
    icon: Painter,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .thenIfNotNull(onClick) { Modifier.clickable(onClick = it) }
            .padding(MaterialTheme.spacing.medium)
    ) {
        Box(
            modifier = Modifier
                .size(MaterialTheme.sizes.large),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.secondary)
            ) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(MaterialTheme.sizes.small)
                )
            }

            ColorTriangle(
                color = color,
                size = 16.dp,
                modifier = Modifier.fillMaxSize(),
            )
        }

        Spacer(Modifier.width(MaterialTheme.spacing.medium))

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f),
        ) {

            if (label.isNotBlank()) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(Modifier.height(MaterialTheme.spacing.extraExtraSmall))
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )

            if (text.isNotBlank()) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AccountCardTilePreview() {
    AppTheme {
        val ore = DisplayableOreValue.of(989898)
        AccountCardTile(
            icon = CardIcons.BANK_CARD.asPainter(),
            color = CardColors.GREEN.asColor(),
            label = "12345 • pipipupu",
            title = stringResource(Res.string.x_of_ore, ore.value).uppercase().asFormattedAmount(),
            text = ore.asString(),
            modifier = Modifier.fillMaxWidth()
                .padding(MaterialTheme.spacing.medium),
        )
    }
}
