package com.wynndie.spwallet.sharedCore.presentation.components.tiles

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
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
import com.wynndie.spwallet.sharedtheme.designSystem.tiles.horizontal.BaseHorizontalTileSmall
import com.wynndie.spwallet.sharedtheme.theme.AppTheme
import com.wynndie.spwallet.sharedtheme.theme.spacing
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TransparentTile(
    icon: Painter,
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    iconBackground: Color = Color.Transparent,
    trailingContent: @Composable (BoxScope.() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    BaseHorizontalTileSmall(
        leadingContent = {
            Image(
                painter = icon,
                contentDescription = title,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
        },
        trailingContent = trailingContent,
        title = title,
        description = description,
        leadingContentMatchConstraints = true,
        leadingContentBackground = Color.Transparent,
        leadingContentShape = CircleShape,
        contentPadding = PaddingValues(MaterialTheme.spacing.medium),
        onClick = onClick,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun TransparentTilePreview() {
    AppTheme {
        TransparentTile(
            icon = CardIcons.BANK_CARD.asImage(),
            iconBackground = CardColors.GREEN.asColor(),
            title = "pipipupu",
            description = "12345",
            onClick = {},
            modifier = Modifier.padding(MaterialTheme.spacing.medium)
        )
    }
}