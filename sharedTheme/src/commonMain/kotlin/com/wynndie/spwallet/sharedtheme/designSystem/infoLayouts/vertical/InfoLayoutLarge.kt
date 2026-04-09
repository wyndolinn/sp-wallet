package com.wynndie.spwallet.sharedtheme.designSystem.infoLayouts.vertical

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wynndie.spwallet.sharedtheme.designSystem.infoLayouts.vertical.base.BaseInfoLayout
import com.wynndie.spwallet.sharedtheme.theme.spacing

@Composable
fun InfoLayoutLarge(
    label: String? = null,
    title: String? = null,
    description: String? = null,
    textAlign: TextAlign = TextAlign.Start,
    image: @Composable (ColumnScope.() -> Unit)? = null,
    action: @Composable (ColumnScope.() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    BaseInfoLayout(
        image = image,
        label = label?.let {
            {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = textAlign,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        title = title?.let {
            {
                Text(
                    text = it,
                    style = MaterialTheme.typography.displayLarge,
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
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = textAlign,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        action = action,
        contentVerticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
        topContentArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
        bodyContentArrangement = Arrangement.spacedBy(0.dp),
        trailingContentArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
        modifier = modifier
    )
}