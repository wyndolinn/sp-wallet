package com.wynndie.spwallet.sharedCore.presentation.component.infoDisplay

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MediumInfoDisplay(
    label: String? = null,
    title: String? = null,
    description: String? = null,
    modifier: Modifier = Modifier
) {
    InfoDisplayScaffold(
        label = label?.let {
            {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        title = title?.let {
            {
                Text(
                    text = it,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        description = description?.let {
            {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        modifier = modifier
    )
}