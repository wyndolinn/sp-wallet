package com.wynndie.spwallet.sharedCore.presentation.components.tiles

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import com.wynndie.spwallet.sharedtheme.designSystem.tiles.horizontal.BaseHorizontalTileSmall

@Composable
fun AppMenuTile(
    icon: Painter,
    title: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    description: String? = null,
    iconBackground: Color = Color.Transparent,
    trailingContent: @Composable (BoxScope.() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    BaseHorizontalTileSmall(
        leadingContent = {
            Image(
                painter = icon,
                contentDescription = label,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
            )
        },
        trailingContent = trailingContent,
        label = label,
        title = title,
        description = description,
        leadingContentBackground = iconBackground,
        onClick = onClick,
        modifier = modifier
    )
}