package com.wynndie.spwallet.sharedCore.presentation.component.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.presentation.component.button.UiTextButton
import com.wynndie.spwallet.sharedCore.presentation.theme.radius
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.cancel
import org.jetbrains.compose.resources.stringResource

@Composable
fun AlertDialogScaffold(
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
            UiTextButton(
                text = confirmButtonText,
                destructive = isDestructive,
                onClick = onConfirm,
            )
        },
        dismissButton = {
            UiTextButton(
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