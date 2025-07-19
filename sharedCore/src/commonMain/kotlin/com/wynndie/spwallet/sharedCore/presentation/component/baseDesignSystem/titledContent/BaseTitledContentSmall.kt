package com.wynndie.spwallet.sharedCore.presentation.component.baseDesignSystem.titledContent

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BaseTitledContentSmall(
    title: String,
    trailingTitledContent: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    BaseTitledContent(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            trailingTitledContent?.let { it() }
        },
        modifier = modifier
    ) {
        content()
    }
}