package com.wynndie.spwallet.sharedtheme.designSystem.titledContent

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BaseTitledContentLarge(
    title: String,
    trailingTitledContent: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    BaseTitledContent(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            trailingTitledContent?.let { it() }
        },
        modifier = modifier
    ) {
        content()
    }
}