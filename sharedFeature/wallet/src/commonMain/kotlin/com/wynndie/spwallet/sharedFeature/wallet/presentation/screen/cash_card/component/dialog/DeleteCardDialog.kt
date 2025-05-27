package com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.cash_card.component.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.cancel
import com.wynndie.spwallet.sharedResources.delete
import com.wynndie.spwallet.sharedResources.delete_card_description
import com.wynndie.spwallet.sharedResources.delete_card_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun DeleteCardDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(Res.string.delete),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(Res.string.cancel))
            }
        },
        title = {
            Text(
                text = stringResource(Res.string.delete_card_title),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth()
            )
        },
        shape = MaterialTheme.shapes.extraLarge,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        text = {
            Column {
                Text(
                    text = stringResource(Res.string.delete_card_description),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}