package com.wynndie.spwallet.sharedCore.presentation.component.appDesignSystem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.presentation.component.baseDesignSystem.BaseCarousel
import com.wynndie.spwallet.sharedCore.presentation.model.Tile
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing

@Composable
fun AppCardCarousel(
    items: List<Tile>,
    page: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: ((Tile) -> Unit)? = null
) {
    BaseCarousel(
        items = items,
        page = page,
        enabled = enabled,
        modifier = modifier
    ) { tile ->
        Box(
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
        ) {
            AppCardTile(
                icon = tile.icon.value,
                iconBackground = tile.iconBackground.value,
                label = tile.label,
                title = tile.title,
                description = tile.description,
                onClick = onClick?.let { { it(tile) } },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}