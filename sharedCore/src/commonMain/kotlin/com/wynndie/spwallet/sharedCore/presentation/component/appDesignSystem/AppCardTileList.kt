package com.wynndie.spwallet.sharedCore.presentation.component.appDesignSystem

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.presentation.component.baseDesignSystem.BaseVerticalList
import com.wynndie.spwallet.sharedCore.presentation.model.Tile

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
            icon = tile.icon.value,
            iconBackground = tile.iconBackground.value,
            label = tile.label,
            title = tile.title,
            description = tile.description,
            onClick = onClickTile?.let { { it(tile) } },
            modifier = Modifier.fillMaxWidth()
        )
    }
}