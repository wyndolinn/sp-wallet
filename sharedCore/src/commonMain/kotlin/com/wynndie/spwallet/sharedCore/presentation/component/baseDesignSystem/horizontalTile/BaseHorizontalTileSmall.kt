package com.wynndie.spwallet.sharedCore.presentation.component.baseDesignSystem.horizontalTile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.wynndie.spwallet.sharedCore.presentation.component.baseDesignSystem.infoPanel.BaseInfoPanelSmall
import com.wynndie.spwallet.sharedCore.presentation.theme.radius
import com.wynndie.spwallet.sharedCore.presentation.theme.size
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing

@Composable
fun BaseHorizontalTileSmall(
    leadingContent: @Composable BoxScope.() -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    trailingContent: @Composable (BoxScope.() -> Unit)? = null,
    label: String? = null,
    description: String? = null,
    leadingContentBackground: Color = MaterialTheme.colorScheme.secondary,
    onClick: (() -> Unit)? = null,
) {

    val minContentHeight = MaterialTheme.size.small

    BaseHorizontalTile(
        leadingContent = {
            Box(
                contentAlignment = Alignment.Center,
                content = leadingContent,
                modifier = Modifier
                    .size(minContentHeight)
                    .clip(MaterialTheme.radius.small)
                    .background(leadingContentBackground)
            )
        },
        trailingContent = trailingContent?.let {
            {
                Box(
                    contentAlignment = Alignment.Center,
                    content = it,
                    modifier = Modifier.size(minContentHeight)
                )
            }
        },
        modifier = modifier
            .then(onClick?.let { Modifier.clickable(onClick = onClick) } ?: Modifier)
            .padding(MaterialTheme.spacing.medium)
    ) {
        BaseInfoPanelSmall(
            label = label,
            title = title,
            description = description,
            modifier = Modifier.sizeIn(minHeight = minContentHeight)
        )
    }
}