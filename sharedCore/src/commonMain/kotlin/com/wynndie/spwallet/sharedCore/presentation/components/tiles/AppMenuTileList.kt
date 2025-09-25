package com.wynndie.spwallet.sharedCore.presentation.components.tiles

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.presentation.extensions.asColor
import com.wynndie.spwallet.sharedCore.presentation.extensions.asImage
import com.wynndie.spwallet.sharedCore.presentation.models.Tile
import com.wynndie.spwallet.sharedtheme.designSystem.BaseVerticalList
import com.wynndie.spwallet.sharedtheme.theme.spacing

@Composable
fun AppMenuTileList(
    title: String,
    tiles: List<Tile>,
    modifier: Modifier = Modifier,
    onClickTile: ((Tile) -> Unit)? = null
) {
    BaseVerticalList(
        title = title,
        items = tiles,
        tileSpacing = MaterialTheme.spacing.none,
        modifier = modifier
    ) { tile ->
        AppMenuTile(
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