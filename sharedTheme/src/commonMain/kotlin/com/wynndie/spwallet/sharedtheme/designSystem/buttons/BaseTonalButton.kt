package com.wynndie.spwallet.sharedtheme.designSystem.buttons

import androidx.compose.foundation.layout.height
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.wynndie.spwallet.sharedtheme.theme.sizing

@Composable
fun BaseTonalButton(
    text: String,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilledTonalButton(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.height(MaterialTheme.sizing.medium)
    ) {
        leadingIcon?.let {
            Icon(
                imageVector = it,
                contentDescription = null
            )
        }

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )

        trailingIcon?.let {
            Icon(
                imageVector = it,
                contentDescription = null
            )
        }
    }
}