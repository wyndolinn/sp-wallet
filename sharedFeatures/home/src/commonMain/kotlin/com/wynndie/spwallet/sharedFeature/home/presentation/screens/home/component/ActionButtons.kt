package com.wynndie.spwallet.sharedFeature.home.presentation.screens.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCard
import androidx.compose.material.icons.outlined.Directions
import androidx.compose.material.icons.outlined.DirectionsTransit
import androidx.compose.material.icons.outlined.EmojiTransportation
import androidx.compose.material.icons.outlined.NoTransfer
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.TransferWithinAStation
import androidx.compose.material.icons.outlined.Transform
import androidx.compose.material.icons.outlined.Transgender
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.activate_card
import com.wynndie.spwallet.sharedResources.transfer_between_cards
import com.wynndie.spwallet.sharedResources.transfer_by_number
import com.wynndie.spwallet.sharedtheme.designSystem.buttons.BaseTonalIconButton
import com.wynndie.spwallet.sharedtheme.theme.spacing
import org.jetbrains.compose.resources.stringResource

@Composable
fun ActionButtons(
    onAuthCardClick: () -> Unit,
    onTransferBetweenCardsClick: () -> Unit,
    onTransferByNumberClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
    ) {
        BaseTonalIconButton(
            icon = Icons.Outlined.AddCard,
            text = stringResource(Res.string.activate_card),
            onClick = { onAuthCardClick() },
            modifier = Modifier.weight(1f)
        )

        BaseTonalIconButton(
            icon = Icons.Outlined.TransferWithinAStation,
            text = stringResource(Res.string.transfer_between_cards),
            onClick = { onTransferBetweenCardsClick() },
            modifier = Modifier.weight(1f)
        )

        BaseTonalIconButton(
            icon = Icons.Outlined.People,
            text = stringResource(Res.string.transfer_by_number),
            onClick = { onTransferByNumberClick() },
            modifier = Modifier.weight(1f)
        )
    }
}