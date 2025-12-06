package com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.transferResult

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedtheme.theme.AppTheme
import com.wynndie.spwallet.sharedtheme.theme.spacing
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun TransferResultScreenRoot(
    viewModel: TransferResultViewModel,
    modifier: Modifier = Modifier
) {

}


@Composable
private fun TransferResultScreenContent(
    state: TransferResultState,
    onAction: (TransferResultAction) -> Unit,
    modifier: Modifier = Modifier
) {

}


@Preview
@Composable
private fun TransferResultScreenPreview() {
    AppTheme {
        TransferResultScreenContent(
            state = TransferResultState(),
            onAction = { },
            modifier = Modifier.padding(MaterialTheme.spacing.medium)
        )
    }
}