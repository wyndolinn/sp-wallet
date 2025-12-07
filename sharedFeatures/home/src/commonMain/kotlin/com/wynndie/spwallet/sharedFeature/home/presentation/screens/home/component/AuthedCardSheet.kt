package com.wynndie.spwallet.sharedFeature.home.presentation.screens.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.presentation.components.tiles.cards.AuthedCardTile
import com.wynndie.spwallet.sharedCore.presentation.extensions.asColor
import com.wynndie.spwallet.sharedCore.presentation.extensions.asImage
import com.wynndie.spwallet.sharedCore.presentation.models.cards.AuthedCardUi
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.deactivate
import com.wynndie.spwallet.sharedResources.ic_people
import com.wynndie.spwallet.sharedResources.ic_transaction
import com.wynndie.spwallet.sharedResources.transfer_between_cards
import com.wynndie.spwallet.sharedResources.transfer_by_number
import com.wynndie.spwallet.sharedtheme.designSystem.buttons.TextButton
import com.wynndie.spwallet.sharedtheme.designSystem.buttons.TonalIconButton
import com.wynndie.spwallet.sharedtheme.designSystem.lists.BaseCarousel
import com.wynndie.spwallet.sharedtheme.designSystem.overlays.BottomSheet
import com.wynndie.spwallet.sharedtheme.theme.spacing
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthedCardSheet(
    onDismiss: () -> Unit,
    cards: List<AuthedCardUi>,
    page: Int,
    onDeleteButtonClick: () -> Unit,
    onTransferBetweenCardsClick: (String) -> Unit,
    onTransferButtonClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BottomSheet(
        onDismiss = onDismiss
    ) {
        AuthedCardSheetContent(
            cards = cards,
            page = page,
            onDeleteButtonClick = onDeleteButtonClick,
            onTransferButtonClick = onTransferButtonClick,
            onTransferBetweenCardsClick = onTransferButtonClick,
            modifier = modifier
        )
    }
}

@Composable
private fun AuthedCardSheetContent(
    cards: List<AuthedCardUi>,
    page: Int,
    onDeleteButtonClick: () -> Unit,
    onTransferBetweenCardsClick: (String) -> Unit,
    onTransferButtonClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            BaseCarousel(
                items = cards,
                page = page,
                modifier = Modifier
            ) { card ->
                Box(
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
                ) {
                    AuthedCardTile(
                        icon = card.icon.asImage(),
                        iconBackground = card.color.asColor(),
                        cardName = card.name,
                        cardNumber = card.number,
                        balance = card.balance,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
            ) {
                TonalIconButton(
                    icon = painterResource(Res.drawable.ic_transaction),
                    text = stringResource(Res.string.transfer_between_cards),
                    onClick = { onTransferBetweenCardsClick(cards[page].id) },
                    modifier = Modifier.weight(1f)
                )

                TonalIconButton(
                    icon = painterResource(Res.drawable.ic_people),
                    text = stringResource(Res.string.transfer_by_number),
                    onClick = { onTransferButtonClick(cards[page].id) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        TextButton(
            text = stringResource(Res.string.deactivate),
            hasError = true,
            onClick = onDeleteButtonClick,
            modifier = Modifier
        )
    }
}