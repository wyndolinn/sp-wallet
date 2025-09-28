package com.wynndie.spwallet.sharedCore.presentation.components.tiles

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
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
import com.wynndie.spwallet.sharedCore.presentation.extensions.asColor
import com.wynndie.spwallet.sharedCore.presentation.extensions.asImage
import com.wynndie.spwallet.sharedCore.presentation.extensions.joinToUiText
import com.wynndie.spwallet.sharedCore.presentation.formatters.displayableValue.OreDisplayableValue
import com.wynndie.spwallet.sharedCore.presentation.formatters.formatAsAmount
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.x_of_ore
import com.wynndie.spwallet.sharedtheme.designSystem.tiles.horizontal.BaseHorizontalTileMedium
import com.wynndie.spwallet.sharedtheme.theme.AppTheme
import com.wynndie.spwallet.sharedtheme.theme.spacing
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FilledTile(
    icon: Painter,
    iconBackground: Color,
    title: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    description: String? = null,
    onClick: (() -> Unit)? = null
) {
    BaseHorizontalTileMedium(
        leadingContent = {
            Image(
                painter = icon,
                contentDescription = label,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
            )
        },
        label = label,
        title = title,
        description = description,
        leadingContentBackground = iconBackground,
        leadingContentShape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(MaterialTheme.spacing.medium),
        onClick = onClick,
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
    )
}

@Preview(showBackground = true)
@Composable
private fun FilledTilePreview() {
    AppTheme {
        FilledTile(
            icon = CardIcons.BANK_CARD.asImage(),
            iconBackground = CardColors.GREEN.asColor(),
            label = "pipipupu • 12345",
            title = stringResource(
                Res.string.x_of_ore,
                OreDisplayableValue.of(555555).value.toString().formatAsAmount()
            ).uppercase(),
            description = OreDisplayableValue.of(555555).formatted.joinToUiText(" ").asString(),
            onClick = {},
            modifier = Modifier.padding(MaterialTheme.spacing.medium)
        )
    }
}