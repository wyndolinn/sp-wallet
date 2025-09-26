package com.wynndie.spwallet.sharedtheme.designSystem.buttons

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.wynndie.spwallet.sharedtheme.theme.sizing

@Composable
fun BaseTextButton(
    text: String,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    destructive: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.height(MaterialTheme.sizing.small)
    ) {
        leadingIcon?.let {
            Icon(
                imageVector = it,
                contentDescription = null
            )
        }

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = if (destructive) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.primary
            }
        )

        trailingIcon?.let {
            Icon(
                imageVector = it,
                contentDescription = null
            )
        }
    }
}