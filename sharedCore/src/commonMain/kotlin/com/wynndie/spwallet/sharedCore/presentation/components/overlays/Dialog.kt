package com.wynndie.spwallet.sharedCore.presentation.components.overlays

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.cancel
import com.wynndie.spwallet.sharedCore.presentation.components.buttons.TextButton
import org.jetbrains.compose.resources.stringResource

@Composable
fun Dialog(
    title: String,
    description: String,
    confirmButtonText: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    destructive: Boolean = false
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                text = confirmButtonText,
                destructive = destructive,
                onClick = onConfirm,
            )
        },
        dismissButton = {
            TextButton(
                text = stringResource(Res.string.cancel),
                onClick = onDismiss
            )
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
        },
        shape = MaterialTheme.shapes.medium,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        modifier = modifier
    )
}