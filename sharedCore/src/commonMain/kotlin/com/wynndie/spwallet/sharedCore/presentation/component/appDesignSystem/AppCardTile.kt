package com.wynndie.spwallet.sharedCore.presentation.component.appDesignSystem

import androidx.compose.foundation.background
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.wynndie.spwallet.sharedCore.presentation.component.baseDesignSystem.horizontalTile.BaseHorizontalTileMedium
import com.wynndie.spwallet.sharedCore.presentation.theme.radius

@Composable
fun AppCardTile(
    icon: ImageVector,
    iconBackground: Color,
    title: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    description: String? = null,
    onClick: (() -> Unit)? = null
) {
    BaseHorizontalTileMedium(
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.onPrimary
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