package com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.cash_card.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.presentation.component.button.UiTextButton
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
            UiTextButton(
                text = stringResource(Res.string.delete),
                destructive = true,
                onClick = onDismiss
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
                text = stringResource(Res.string.delete_card_title),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column {
                Text(
                    text = stringResource(Res.string.delete_card_description),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        shape = MaterialTheme.shapes.extraLarge,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    )
}