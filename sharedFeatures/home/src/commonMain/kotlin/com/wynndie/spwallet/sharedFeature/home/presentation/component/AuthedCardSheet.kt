package com.wynndie.spwallet.sharedFeature.home.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.domain.models.cards.AuthedCard
import com.wynndie.spwallet.sharedCore.presentation.components.BaseCarousel
import com.wynndie.spwallet.sharedCore.presentation.components.buttons.OutlinedButton
import com.wynndie.spwallet.sharedCore.presentation.components.buttons.TonalIconButton
import com.wynndie.spwallet.sharedCore.presentation.components.overlays.BottomSheet
import com.wynndie.spwallet.sharedCore.presentation.components.tiles.TransferCardTile
import com.wynndie.spwallet.sharedCore.presentation.extensions.asColor
import com.wynndie.spwallet.sharedCore.presentation.extensions.asPainter
import com.wynndie.spwallet.sharedCore.presentation.extensions.asDisplayableOre
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.deactivate
import com.wynndie.spwallet.sharedResources.ic_delete
import com.wynndie.spwallet.sharedResources.ic_people
import com.wynndie.spwallet.sharedResources.ic_transaction
import com.wynndie.spwallet.sharedResources.transfer_between_cards
import com.wynndie.spwallet.sharedResources.transfer_by_number
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthedCardSheet(
    onDismiss: () -> Unit,
    cards: List<AuthedCard>,
    page: Int,
    onDeleteButtonClick: () -> Unit,
    onTransferBetweenCardsClick: (String) -> Unit,
    onTransferButtonClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BottomSheet(
        onDismiss = onDismiss
    ) {
        var currentPage by remember { mutableStateOf(page) }
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            BaseCarousel(
                items = cards,
                page = page,
                onSwipePage = { currentPage = it },
                contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium),
                pageSpacing = MaterialTheme.spacing.medium
            ) { card ->
                TransferCardTile(
                    title = card.balance.asDisplayableOre().formatted,
                    text = "${card.number} • ${card.name}",
                    icon = card.icon.asPainter(),
                    color = card.color.asColor(),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
            ) {
                TonalIconButton(
                    icon = painterResource(Res.drawable.ic_transaction),
                    label = stringResource(Res.string.transfer_between_cards),
                    onClick = { onTransferBetweenCardsClick(cards[currentPage].id) },
                    modifier = Modifier.weight(1f)
                )

                TonalIconButton(
                    icon = painterResource(Res.drawable.ic_people),
                    label = stringResource(Res.string.transfer_by_number),
                    onClick = { onTransferButtonClick(cards[currentPage].id) },
                    modifier = Modifier.weight(1f)
                )
            }

            OutlinedButton(
                text = stringResource(Res.string.deactivate),
                icon = painterResource(Res.drawable.ic_delete),
                destructive = true,
                onClick = onDeleteButtonClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium)
            )
        }
    }
}