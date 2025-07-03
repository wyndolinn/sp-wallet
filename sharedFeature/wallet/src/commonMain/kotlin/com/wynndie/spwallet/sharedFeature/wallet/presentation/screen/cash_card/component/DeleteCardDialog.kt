package com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.cash_card.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.presentation.component.designSystem.dialog.AlertDialogScaffold
import com.wynndie.spwallet.sharedResources.Res
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
    AlertDialogScaffold(
        onDismiss = onDismiss,
        onConfirm = onConfirm,
        title = stringResource(Res.string.delete_card_title),
        description = stringResource(Res.string.delete_card_description),
        confirmButtonText = stringResource(Res.string.delete),
        isDestructive = true,
        modifier = modifier
    )
}