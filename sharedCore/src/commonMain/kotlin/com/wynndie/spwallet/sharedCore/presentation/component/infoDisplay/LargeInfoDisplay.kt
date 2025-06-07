package com.wynndie.spwallet.sharedCore.presentation.component.infoDisplay

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing

@Composable
fun LargeInfoDisplay(
    image: @Composable (ColumnScope.() -> Unit)? = null,
    label: String? = null,
    title: String? = null,
    description: String? = null,
    action: @Composable (ColumnScope.() -> Unit)? = null,
    textAlign: TextAlign = TextAlign.Start,
    modifier: Modifier = Modifier
) {
    InfoDisplayScaffold(
        image = image?.let {
            {
                it()

                Spacer(Modifier.height(MaterialTheme.spacing.small))
            }
        },
        label = label?.let {
            {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = textAlign,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(MaterialTheme.spacing.medium))
            }
        },
        title = title?.let {
            {
                Text(
                    text = it,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = textAlign,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(MaterialTheme.spacing.extraSmall))
            }
        },
        description = description?.let {
            {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = textAlign,
                    modifier = Modifier.fillMaxWidth()
                )

                if (action != null) Spacer(Modifier.height(MaterialTheme.spacing.medium))
            }
        },
        action = action,
        modifier = modifier
    )
}