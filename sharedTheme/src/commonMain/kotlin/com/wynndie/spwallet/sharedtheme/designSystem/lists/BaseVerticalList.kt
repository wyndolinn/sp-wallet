package com.wynndie.spwallet.sharedtheme.designSystem.lists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.wynndie.spwallet.sharedtheme.designSystem.titledContent.BaseTitledContentMedium
import com.wynndie.spwallet.sharedtheme.theme.spacing

@Composable
fun <T> BaseVerticalList(
    title: String,
    items: List<T>,
    modifier: Modifier = Modifier,
    tileSpacing: Dp = MaterialTheme.spacing.small,
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
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(tileSpacing)
            ) {
                leadingContent?.let { item { it() } }
                items(items) { content(it) }
                trailingContent?.let { item { it() } }
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(tileSpacing)
            ) {
                leadingContent?.let { it() }
                items.forEach { content(it) }
                trailingContent?.let { it() }
            }
        }
    }
}