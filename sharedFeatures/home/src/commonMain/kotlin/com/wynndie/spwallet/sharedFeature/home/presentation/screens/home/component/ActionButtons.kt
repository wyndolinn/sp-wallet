package com.wynndie.spwallet.sharedFeature.home.presentation.screens.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.ic_arrow_front
import com.wynndie.spwallet.sharedCore.ic_people
import com.wynndie.spwallet.sharedCore.ic_transaction
import com.wynndie.spwallet.sharedCore.presentation.components.buttons.LabeledIconButton
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedCore.recipient
import com.wynndie.spwallet.sharedCore.recipients
import com.wynndie.spwallet.sharedCore.transfer_between_cards
import com.wynndie.spwallet.sharedCore.transfer_by_number
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ActionButtons(
    onRecipientsClick: () -> Unit,
    onTransferBetweenCardsClick: () -> Unit,
    onTransferByNumberClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
    ) {
        LabeledIconButton(
            icon = painterResource(Res.drawable.ic_people),
            label = stringResource(Res.string.recipients),
            onClick = onRecipientsClick,
            modifier = Modifier.weight(1f)
        )

        LabeledIconButton(
            icon = painterResource(Res.drawable.ic_arrow_front),
            label = stringResource(Res.string.transfer_by_number),
            onClick = onTransferByNumberClick,
            modifier = Modifier.weight(1f)
        )

        LabeledIconButton(
            icon = painterResource(Res.drawable.ic_transaction),
            label = stringResource(Res.string.transfer_between_cards),
            onClick = onTransferBetweenCardsClick,
            modifier = Modifier.weight(1f)
        )
    }
}