package com.wynndie.spwallet.sharedCore.presentation.component.card

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
import com.wynndie.spwallet.sharedCore.presentation.component.infoDisplay.SmallInfoDisplay
import com.wynndie.spwallet.sharedCore.presentation.theme.radius
import com.wynndie.spwallet.sharedCore.presentation.theme.size
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing

@Composable
fun HorizontalSmallCard(
    leadingContent: @Composable BoxScope.() -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    trailingContent: @Composable BoxScope.() -> Unit = {},
    label: String? = null,
    description: String? = null,
    leadingContentBackground: Color = MaterialTheme.colorScheme.tertiary,
    onClick: (() -> Unit)? = null,
) {

    val minContentHeight = MaterialTheme.size.small

    HorizontalCardScaffold(
        leadingContent = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(minContentHeight)
                    .clip(MaterialTheme.radius.small)
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
        SmallInfoDisplay(
            label = label,
            title = title,
            description = description,
            modifier = Modifier.sizeIn(minHeight = minContentHeight)
        )
    }
}