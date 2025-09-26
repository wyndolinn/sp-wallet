package com.wynndie.spwallet.sharedtheme.designSystem.tiles.horizontal

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
import com.wynndie.spwallet.sharedtheme.designSystem.infoLayouts.vertical.BaseInfoPanelSmall
import com.wynndie.spwallet.sharedtheme.extensions.factor
import com.wynndie.spwallet.sharedtheme.theme.sizing
import com.wynndie.spwallet.sharedtheme.theme.spacing

@Composable
fun BaseHorizontalTileLarge(
    leadingContent: @Composable BoxScope.() -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    trailingContent: @Composable BoxScope.() -> Unit = {},
    label: String? = null,
    description: String? = null,
    leadingContentBackground: Color = MaterialTheme.colorScheme.secondary,
    onClick: (() -> Unit)? = null,
) {

    val minContentHeight = MaterialTheme.sizing.small.factor(2f)

    BaseHorizontalTile(
        leadingContent = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(minContentHeight)
                    .clip(MaterialTheme.shapes.small)
                    .background(leadingContentBackground)
            ) {
                leadingContent()
            }
        },
        trailingContent = {
            Box(
                contentAlignment = Alignment.Center,
                propagateMinConstraints = true
            ) {
                trailingContent()
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