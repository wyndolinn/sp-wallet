package com.wynndie.spwallet.sharedFeature.edit.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.delete
import com.wynndie.spwallet.sharedCore.presentation.components.overlays.Dialog
import org.jetbrains.compose.resources.stringResource

@Composable
fun DeleteCardDialog(
    title: String,
    description: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismiss = onDismiss,
        onConfirm = onConfirm,
        title = title,
        description = description,
        confirmButtonText = stringResource(Res.string.delete),
        destructive = true,
        modifier = modifier
    )
}