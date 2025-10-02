package com.wynndie.spwallet.sharedtheme.designSystem.tiles.horizontal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.wynndie.spwallet.sharedtheme.designSystem.tiles.horizontal.base.BaseHorizontalTile
import com.wynndie.spwallet.sharedtheme.extensions.ContentState
import com.wynndie.spwallet.sharedtheme.extensions.asDescriptionColor
import com.wynndie.spwallet.sharedtheme.extensions.asLabelColor
import com.wynndie.spwallet.sharedtheme.extensions.asTitleColor
import com.wynndie.spwallet.sharedtheme.theme.RectangleShape
import com.wynndie.spwallet.sharedtheme.theme.sizing
import com.wynndie.spwallet.sharedtheme.theme.spacing

@Composable
fun HorizontalTileMedium(
    title: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    description: String? = null,
    action: @Composable (() -> Unit)? = null,
    contentState: ContentState = ContentState.Neutral,
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

    val minContentHeight = MaterialTheme.sizing.medium

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
        Column(
            modifier = Modifier.sizeIn(minHeight = minContentHeight),
            verticalArrangement = Arrangement.Center
        ) {

            label?.let {
                Text(
                    modifier = Modifier,
                    text = it,
                    style = MaterialTheme.typography.labelLarge,
                    color = contentState.asLabelColor()
                )

                Spacer(Modifier.height(MaterialTheme.spacing.extraSmall))
            }

            Text(
                modifier = Modifier,
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = contentState.asTitleColor()
            )

            description?.let {
                Text(
                    modifier = Modifier,
                    text = it,
                    style = MaterialTheme.typography.labelMedium,
                    color = contentState.asDescriptionColor()
                )
            }

            action?.let {
                Spacer(Modifier.height(MaterialTheme.spacing.extraSmall))

                it()
            }
        }
    }
}