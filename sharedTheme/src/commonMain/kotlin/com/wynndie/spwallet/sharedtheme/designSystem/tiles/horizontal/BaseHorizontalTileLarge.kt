package com.wynndie.spwallet.sharedtheme.designSystem.tiles.horizontal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.wynndie.spwallet.sharedtheme.designSystem.infoLayouts.vertical.BaseInfoPanelSmall
import com.wynndie.spwallet.sharedtheme.extensions.factor
import com.wynndie.spwallet.sharedtheme.theme.RectangleShape
import com.wynndie.spwallet.sharedtheme.theme.sizing
import com.wynndie.spwallet.sharedtheme.theme.spacing

@Composable
fun BaseHorizontalTileLarge(
    title: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    description: String? = null,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    contentPadding: PaddingValues = PaddingValues(MaterialTheme.spacing.medium),
    leadingContent: @Composable (BoxScope.() -> Unit)? = null,
    leadingContentShape: CornerBasedShape = RectangleShape,
    leadingContentBackground: Color = Color.Transparent,
    leadingContentMatchConstraints: Boolean = false,
    trailingContent: @Composable (BoxScope.() -> Unit)? = null,
    trailingContentShape: CornerBasedShape = RectangleShape,
    trailingContentBackground: Color = Color.Transparent,
    trailingContentMatchConstraints: Boolean = false,
    onClick: (() -> Unit)? = null
) {

    val minContentHeight = MaterialTheme.sizing.small.factor(2f)

    BaseHorizontalTile(
        leadingContent = leadingContent?.let {
            {
                Box(
                    contentAlignment = Alignment.Center,
                    propagateMinConstraints = leadingContentMatchConstraints,
                    content = it,
                    modifier = Modifier
                        .size(minContentHeight)
                        .clip(leadingContentShape)
                        .background(leadingContentBackground)
                )
            }
        },
        trailingContent = trailingContent?.let {
            {
                Box(
                    contentAlignment = Alignment.Center,
                    propagateMinConstraints = trailingContentMatchConstraints,
                    content = it,
                    modifier = Modifier
                        .size(minContentHeight)
                        .clip(trailingContentShape)
                        .background(trailingContentBackground)
                )
            }
        },
        verticalAlignment = verticalAlignment,
        contentPadding = contentPadding,
        onClick = onClick,
        modifier = modifier
    ) {
        BaseInfoPanelSmall(
            label = label,
            title = title,
            description = description,
            modifier = Modifier.sizeIn(minHeight = minContentHeight)
        )
    }
}