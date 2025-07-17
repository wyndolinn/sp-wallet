package com.wynndie.spwallet.sharedFeature.home.presentation.component

import androidx.compose.foundation.background
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.wynndie.spwallet.sharedCore.presentation.component.designSystem.horizontalCard.HorizontalMediumCard
import com.wynndie.spwallet.sharedCore.presentation.theme.radius

@Composable
fun UiCardItem(
    icon: ImageVector,
    iconBackground: Color,
    label: String,
    title: String,
    description: String? = null,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    HorizontalMediumCard(
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