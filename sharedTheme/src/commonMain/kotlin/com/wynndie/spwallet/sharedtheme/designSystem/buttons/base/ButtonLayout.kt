package com.wynndie.spwallet.sharedtheme.designSystem.buttons.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import com.wynndie.spwallet.sharedtheme.theme.spacing

@Composable
internal fun ButtonLayout(
    text: String,
    leadingIcon: Painter? = null,
    trailingIcon: Painter? = null,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(
            space = MaterialTheme.spacing.small,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        leadingIcon?.let {
            Image(
                painter = it,
                contentDescription = null
            )
        }

        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )

        trailingIcon?.let {
            Image(
                painter = it,
                contentDescription = null
            )
        }
    }
}