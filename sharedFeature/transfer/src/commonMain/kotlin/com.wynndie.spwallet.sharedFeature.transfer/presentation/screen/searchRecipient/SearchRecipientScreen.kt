package com.wynndie.spwallet.sharedFeature.transfer.presentation.screen.searchRecipient

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.presentation.component.baseDesignSystem.BaseInputField

@Composable
fun SearchRecipientScreenRoot(
    modifier: Modifier = Modifier
) {

}

@Composable
private fun SearchRecipientScreenContent(
    state: SearchRecipientState,
    onAction: (SearchRecipientAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        BaseInputField(

        )
    }
}