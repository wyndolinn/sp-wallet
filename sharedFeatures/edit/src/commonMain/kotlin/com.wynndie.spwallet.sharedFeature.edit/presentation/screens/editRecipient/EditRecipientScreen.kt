package com.wynndie.spwallet.sharedFeature.edit.presentation.screens.editRecipient

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedtheme.theme.AppTheme
import com.wynndie.spwallet.sharedtheme.theme.spacing
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EditRecipientScreenRoot(
    modifier: Modifier = Modifier
) {

}

@Composable
private fun EditRecipientScreen(
    modifier: Modifier = Modifier
) {

}

@Preview(showBackground = true)
@Composable
private fun EditRecipientScreenPreview() {
    AppTheme {
        EditRecipientScreen(
            modifier = Modifier.padding(MaterialTheme.spacing.medium)
        )
    }
}