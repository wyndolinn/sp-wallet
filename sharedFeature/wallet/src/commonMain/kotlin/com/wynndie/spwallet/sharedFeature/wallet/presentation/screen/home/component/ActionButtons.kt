package com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCard
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.presentation.component.designSystem.button.UiTonalIconButton
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.activate_card
import com.wynndie.spwallet.sharedResources.transfer_by_number
import org.jetbrains.compose.resources.stringResource

@Composable
fun ActionButtons(
    onAuthCardClick: () -> Unit,
    onTransferByNumberClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
    ) {
        UiTonalIconButton(
            icon = Icons.Outlined.AddCard,
            text = stringResource(Res.string.activate_card),
            onClick = { onAuthCardClick() },
            modifier = Modifier.weight(1f)
        )

        UiTonalIconButton(
            icon = Icons.Outlined.People,
            text = stringResource(Res.string.transfer_by_number),
            onClick = { onTransferByNumberClick() },
            modifier = Modifier.weight(1f)
        )
    }
}