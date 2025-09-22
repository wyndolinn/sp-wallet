package com.wynndie.spwallet.sharedFeature.home.presentation.screen.cash_card.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.delete
import com.wynndie.spwallet.sharedResources.delete_card_description
import com.wynndie.spwallet.sharedResources.delete_card_title
import com.wynndie.spwallet.sharedtheme.designSystem.BaseAlertDialogLayout
import org.jetbrains.compose.resources.stringResource

@Composable
fun DeleteCardDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    BaseAlertDialogLayout(
        onDismiss = onDismiss,
        onConfirm = onConfirm,
        title = stringResource(Res.string.delete_card_title),
        description = stringResource(Res.string.delete_card_description),
        confirmButtonText = stringResource(Res.string.delete),
        isDestructive = true,
        modifier = modifier
    )
}