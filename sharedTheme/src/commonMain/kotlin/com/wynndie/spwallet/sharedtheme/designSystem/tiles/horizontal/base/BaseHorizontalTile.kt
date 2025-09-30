package com.wynndie.spwallet.sharedtheme.designSystem.tiles.horizontal.base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedtheme.extensions.thenIfNotNull
import com.wynndie.spwallet.sharedtheme.theme.spacing

@Composable
internal fun BaseHorizontalTile(
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    contentPadding: PaddingValues = PaddingValues(MaterialTheme.spacing.medium),
    onClick: (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        verticalAlignment = verticalAlignment,
        modifier = modifier
            .thenIfNotNull(onClick) { Modifier.clickable(onClick = it) }
            .padding(contentPadding)
    ) {
        leadingContent?.let { it() }

        Box(
            content = { content() },
            modifier = Modifier.weight(1f)
        )

        trailingContent?.let { it() }
    }
}