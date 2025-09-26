package com.wynndie.spwallet.sharedtheme.designSystem.overlays

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.cancel
import com.wynndie.spwallet.sharedtheme.designSystem.buttons.BaseTextButton
import com.wynndie.spwallet.sharedtheme.theme.radius
import org.jetbrains.compose.resources.stringResource

@Composable
fun BaseDialogLayout(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    description: String,
    confirmButtonText: String,
    modifier: Modifier = Modifier,
    isDestructive: Boolean = false
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            BaseTextButton(
                text = confirmButtonText,
                destructive = isDestructive,
                onClick = onConfirm,
            )
        },
        dismissButton = {
            BaseTextButton(
                text = stringResource(Res.string.cancel),
                onClick = onDismiss
            )
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        shape = MaterialTheme.radius.default,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        modifier = modifier
    )
}