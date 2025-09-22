package com.wynndie.spwallet.sharedCore.presentation.component.appDesignSystem

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.wynndie.spwallet.sharedtheme.designSystem.horizontalTile.BaseHorizontalTileSmall

@Composable
fun AppMenuTile(
    icon: ImageVector,
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
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.onPrimary
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