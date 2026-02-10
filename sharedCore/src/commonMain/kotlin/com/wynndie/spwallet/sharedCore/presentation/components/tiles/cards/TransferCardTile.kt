package com.wynndie.spwallet.sharedCore.presentation.components.tiles.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wynndie.spwallet.sharedCore.presentation.extensions.cardColorGradient
import com.wynndie.spwallet.sharedCore.presentation.formatters.displayableValue.OreDisplayableValue
import com.wynndie.spwallet.sharedCore.presentation.formatters.formatAsAmount
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.x_of_ore
import com.wynndie.spwallet.sharedtheme.extensions.thenIfNotNull
import com.wynndie.spwallet.sharedtheme.theme.sizing
import com.wynndie.spwallet.sharedtheme.theme.spacing
import org.jetbrains.compose.resources.stringResource

@Composable
fun TransferCardTile(
    name: String,
    number: String,
    color: Color,
    icon: Painter,
    modifier: Modifier = Modifier,
    title: String = "",
    balance: OreDisplayableValue? = null,
    onClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .cardColorGradient(color)
            .thenIfNotNull(onClick) { Modifier.clickable(onClick = it) }
            .padding(MaterialTheme.spacing.medium)
    ) {
        if (title.isNotBlank()) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight(600)
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
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                modifier = Modifier.size(MaterialTheme.sizing.large)
            )

            Column {
                Text(
                    text = if (balance != null) "$number • $name" else name,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.alpha(0.8f)
                )

                Text(
                    text = if (balance != null) {
                        stringResource(Res.string.x_of_ore, balance.value)
                            .uppercase()
                            .formatAsAmount()
                    } else number,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight(600)
                )
            }
        }
    }
}