package com.wynndie.spwallet.sharedCore.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedtheme.designSystem.titledContent.BaseTitledContent
import com.wynndie.spwallet.sharedtheme.extensions.ContentState
import com.wynndie.spwallet.sharedtheme.theme.spacing

@Composable
fun TitledContent(
    title: String,
    modifier: Modifier = Modifier,
    state: ContentState = ContentState.Neutral,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(MaterialTheme.spacing.small),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    titlePadding: PaddingValues = PaddingValues(),
    trailingContent: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {

    val titleColor by animateColorAsState(
        targetValue = when (state) {
            ContentState.Neutral -> MaterialTheme.colorScheme.onSurface
            ContentState.Disabled -> MaterialTheme.colorScheme.outline
            ContentState.Selected -> MaterialTheme.colorScheme.primary
            ContentState.Alerted -> MaterialTheme.colorScheme.error
        }
    )

    BaseTitledContent(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = titleColor
            )

            trailingContent?.let { it() }
        },
        titlePadding = titlePadding,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        content = content,
        modifier = modifier
    )
}