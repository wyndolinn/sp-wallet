package com.wynndie.spwallet.sharedCore.presentation.components.tiles

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import com.wynndie.spwallet.sharedtheme.designSystem.tiles.horizontal.BaseHorizontalTileMedium
import com.wynndie.spwallet.sharedtheme.theme.radius

@Composable
fun AppCardTile(
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
        modifier = modifier
            .clip(MaterialTheme.radius.default)
            .background(color = MaterialTheme.colorScheme.surfaceContainer),
        leadingContentBackground = iconBackground,
        onClick = onClick
    )
}