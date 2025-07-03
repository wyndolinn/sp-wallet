package com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.home.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.presentation.component.designSystem.dialog.AlertDialogScaffold
import com.wynndie.spwallet.sharedResources.Res
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
    AlertDialogScaffold(
        onDismiss = onDismiss,
        onConfirm = onConfirm,
        title = stringResource(Res.string.deactivate_card_title),
        description = stringResource(Res.string.deactivate_card_description),
        confirmButtonText = stringResource(Res.string.deactivate),
        isDestructive = true,
        modifier = modifier
    )
}