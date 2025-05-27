package com.wynndie.spwallet.sharedCore.presentation.component.titledContent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing

@Composable
fun <T> VerticalTitledList(
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
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
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
                Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
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