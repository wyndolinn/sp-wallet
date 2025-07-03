package com.wynndie.spwallet.sharedCore.presentation.component.designSystem.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.presentation.component.designSystem.titledContent.TitledMediumContent
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing

@Composable
fun <T> HorizontalTitledList(
    title: String,
    items: List<T>,
    modifier: Modifier = Modifier,
    isScrollable: Boolean = false,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    content: @Composable (T) -> Unit
) {
    TitledMediumContent(
        title = title,
        modifier = modifier
    ) {
        when {
            isScrollable -> {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                ) {
                    leadingContent?.let {
                        item { it() }
                    }

                    items(items) { item ->
                        content(item)
                    }

                    trailingContent?.let {
                        item { it() }
                    }
                }
            }

            !isScrollable -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                ) {
                    leadingContent?.let {
                        it()
                    }

                    items.forEach { item ->
                        content(item)
                    }

                    trailingContent?.let {
                        it()
                    }
                }
            }
        }
    }
}