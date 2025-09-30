package com.wynndie.spwallet.sharedtheme.designSystem.titledContent

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.wynndie.spwallet.sharedtheme.designSystem.titledContent.base.BaseTitledContent

@Composable
fun TitledContent(
    title: String,
    trailingTitledContent: (@Composable () -> Unit)? = null,
    titlePadding: PaddingValues = PaddingValues(),
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    color: Color = MaterialTheme.colorScheme.onSurface,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    BaseTitledContent(
        title = {
            CompositionLocalProvider(LocalContentColor provides color) {
                Text(
                    text = title,
                    style = style,
                    color = LocalContentColor.current
                )

                trailingTitledContent?.let { it() }
            }
        },
        titlePadding = titlePadding,
        modifier = modifier
    ) {
        content()
    }
}