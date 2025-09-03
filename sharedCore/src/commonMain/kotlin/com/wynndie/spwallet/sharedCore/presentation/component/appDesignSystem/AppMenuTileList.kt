package com.wynndie.spwallet.sharedCore.presentation.component.appDesignSystem

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.presentation.component.baseDesignSystem.BaseVerticalList
import com.wynndie.spwallet.sharedCore.presentation.model.Tile
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing

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
            icon = tile.icon.value,
            label = tile.label,
            title = tile.title,
            description = tile.description,
            onClick = onClickTile?.let { { it(tile) } },
            modifier = Modifier.fillMaxWidth()
        )
    }
}