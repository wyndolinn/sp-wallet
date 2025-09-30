package com.wynndie.spwallet.sharedFeature.home.presentation.screens.home.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.deactivate
import com.wynndie.spwallet.sharedResources.deactivate_card_description
import com.wynndie.spwallet.sharedResources.deactivate_card_title
import com.wynndie.spwallet.sharedtheme.designSystem.overlays.Dialog
import org.jetbrains.compose.resources.stringResource

@Composable
fun DeactivateCardDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismiss = onDismiss,
        onConfirm = onConfirm,
        title = stringResource(Res.string.deactivate_card_title),
        description = stringResource(Res.string.deactivate_card_description),
        confirmButtonText = stringResource(Res.string.deactivate),
        isDestructive = true,
        modifier = modifier
    )
}