package com.wynndie.spwallet.sharedtheme.designSystem.infoLayouts.vertical

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.wynndie.spwallet.sharedtheme.theme.spacing

@Composable
fun BaseInfoPanelMedium(
    label: String? = null,
    title: String? = null,
    description: String? = null,
    textAlign: TextAlign = TextAlign.Start,
    image: @Composable (ColumnScope.() -> Unit)? = null,
    action: @Composable (ColumnScope.() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    BaseInfoLayout(
        image = image?.let {
            {
                it()

                Spacer(Modifier.height(MaterialTheme.spacing.small))
            }
        },
        label = label?.let {
            {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = textAlign,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(MaterialTheme.spacing.extraSmall))
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
            }
        },
        description = description?.let {
            {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = textAlign,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(MaterialTheme.spacing.small))
            }
        },
        action = action,
        modifier = modifier
    )
}