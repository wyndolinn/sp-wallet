package com.wynndie.spwallet.sharedtheme.designSystem.titledContent

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wynndie.spwallet.sharedtheme.designSystem.titledContent.base.BaseTitledContent
import com.wynndie.spwallet.sharedtheme.theme.spacing

@Composable
fun TitledContent(
    title: String,
    titleTrailingContent: (@Composable () -> Unit)? = null,
    titlePadding: PaddingValues = PaddingValues(
        horizontal = MaterialTheme.spacing.medium
    ),
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    BaseTitledContent(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight(600),
                modifier = Modifier.weight(1f)
            )

            titleTrailingContent?.let { it() }
        },
        titlePadding = titlePadding,
        content = content,
        modifier = modifier
    )
}