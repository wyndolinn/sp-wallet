package com.wynndie.spwallet.sharedCore.presentation.component.baseDesignSystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.presentation.component.baseDesignSystem.titledContent.BaseTitledContentMedium
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing

@Composable
fun <T> BaseHorizontalList(
    title: String,
    items: List<T>,
    modifier: Modifier = Modifier,
    scrollable: Boolean = false,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    content: @Composable (T) -> Unit
) {
    BaseTitledContentMedium(
        title = title,
        modifier = modifier
    ) {
        if (scrollable) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                leadingContent?.let { item { it() } }
                items(items) { content(it) }
                trailingContent?.let { item { it() } }
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                leadingContent?.let { it() }
                items.forEach { content(it) }
                trailingContent?.let { it() }
            }
        }
    }
}