package com.wynndie.spwallet.sharedFeature.edit.presentation.screens.recipients.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.delete
import com.wynndie.spwallet.sharedCore.domain.models.cards.RecipientCard
import com.wynndie.spwallet.sharedCore.ic_edit
import com.wynndie.spwallet.sharedCore.ic_transaction
import com.wynndie.spwallet.sharedCore.presentation.components.buttons.LabeledIconButton
import com.wynndie.spwallet.sharedCore.presentation.components.buttons.TextButton
import com.wynndie.spwallet.sharedCore.presentation.components.overlays.BottomSheet
import com.wynndie.spwallet.sharedCore.presentation.components.tiles.TransferCardTile
import com.wynndie.spwallet.sharedCore.presentation.extensions.asPainter
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedCore.recipient
import com.wynndie.spwallet.sharedCore.transfer_between_cards
import com.wynndie.spwallet.sharedCore.transfer_by_number
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipientSheet(
    onDismiss: () -> Unit,
    recipient: RecipientCard,
    onDeleteButtonClick: () -> Unit,
    onEditRecipientClick: () -> Unit,
    onTransferClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BottomSheet(
        onDismiss = onDismiss
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            TransferCardTile(
                headline = stringResource(Res.string.recipient),
                title = recipient.number,
                text = recipient.name,
                icon = recipient.icon.asPainter(),
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
            ) {
                LabeledIconButton(
                    icon = painterResource(Res.drawable.ic_edit),
                    label = stringResource(Res.string.transfer_between_cards),
                    onClick = onEditRecipientClick,
                    modifier = Modifier.weight(1f)
                )

                LabeledIconButton(
                    icon = painterResource(Res.drawable.ic_transaction),
                    label = stringResource(Res.string.transfer_by_number),
                    onClick = onTransferClick,
                    modifier = Modifier.weight(1f)
                )
            }

            TextButton(
                text = stringResource(Res.string.delete),
                destructive = true,
                onClick = onDeleteButtonClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium)
            )
        }
    }
}