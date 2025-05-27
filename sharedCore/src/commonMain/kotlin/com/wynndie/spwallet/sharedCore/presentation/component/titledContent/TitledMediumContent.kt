package com.wynndie.spwallet.sharedCore.presentation.component.titledContent

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TitledMediumContent(
    title: String,
    trailingTitledContent: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    TitledContentScaffold(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            trailingTitledContent?.let { it() }
        },
        modifier = modifier
    ) {
        content()
    }
}