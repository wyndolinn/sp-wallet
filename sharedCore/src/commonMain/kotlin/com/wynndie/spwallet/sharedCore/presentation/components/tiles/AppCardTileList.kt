package com.wynndie.spwallet.sharedCore.presentation.components.tiles

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.presentation.extensions.asColor
import com.wynndie.spwallet.sharedCore.presentation.extensions.asImage
import com.wynndie.spwallet.sharedCore.presentation.models.Tile
import com.wynndie.spwallet.sharedtheme.designSystem.BaseVerticalList

@Composable
fun AppCardTileList(
    title: String,
    tiles: List<Tile>,
    modifier: Modifier = Modifier,
    onClickTile: ((Tile) -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
    BaseVerticalList(
        title = title,
        items = tiles,
        trailingContent = trailingContent,
        modifier = modifier
    ) { tile ->
        AppCardTile(
            icon = tile.icon.asImage(),
            iconBackground = tile.iconBackground.asColor(),
            label = tile.label,
            title = tile.title,
            description = tile.description,
            onClick = onClickTile?.let { { it(tile) } },
            modifier = Modifier.fillMaxWidth()
        )
    }
}