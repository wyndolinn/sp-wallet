package com.wynndie.spwallet.sharedCore.presentation.components.tiles

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardIcons
import com.wynndie.spwallet.sharedCore.presentation.extensions.asImage
import com.wynndie.spwallet.sharedtheme.designSystem.tiles.horizontal.HorizontalTileSmall
import com.wynndie.spwallet.sharedtheme.theme.AppTheme

@Composable
fun TransparentTile(
    icon: Painter?,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    trailingContent: @Composable (BoxScope.() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    HorizontalTileSmall(
        leadingContent = icon?.let {
            {
                Image(
                    painter = it,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                    contentDescription = null
                )
            }
        },
        title = title,
        description = description,
        trailingContent = trailingContent,
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
            title = "Title tile",
            description = "Some kind of description"
        )
    }
}