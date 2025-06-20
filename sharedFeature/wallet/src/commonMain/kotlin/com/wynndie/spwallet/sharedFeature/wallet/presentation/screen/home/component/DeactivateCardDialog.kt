package com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.presentation.component.button.UiTextButton
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.cancel
import com.wynndie.spwallet.sharedResources.deactivate
import com.wynndie.spwallet.sharedResources.deactivate_card_description
import com.wynndie.spwallet.sharedResources.deactivate_card_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun DeactivateCardDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            UiTextButton(
                text = stringResource(Res.string.deactivate),
                destructive = true,
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
                text = stringResource(Res.string.deactivate_card_title),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column {
                Text(
                    text = stringResource(Res.string.deactivate_card_description),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        shape = MaterialTheme.shapes.extraLarge,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        modifier = modifier
    )
}