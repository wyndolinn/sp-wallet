package com.wynndie.spwallet.sharedFeature.home.presentation.screens.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.wynndie.spwallet.sharedCore.presentation.components.tiles.cards.AuthedCardTile
import com.wynndie.spwallet.sharedCore.presentation.components.tiles.cards.UnauthedCardTile
import com.wynndie.spwallet.sharedCore.presentation.extensions.asColor
import com.wynndie.spwallet.sharedCore.presentation.extensions.asImage
import com.wynndie.spwallet.sharedCore.presentation.models.cards.AuthedCardUi
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.deactivate
import com.wynndie.spwallet.sharedResources.transfer_by_number
import com.wynndie.spwallet.sharedtheme.designSystem.buttons.BaseTextButton
import com.wynndie.spwallet.sharedtheme.designSystem.buttons.BaseTonalIconButton
import com.wynndie.spwallet.sharedtheme.designSystem.lists.BaseCarousel
import com.wynndie.spwallet.sharedtheme.designSystem.overlays.BaseSheetLayout
import com.wynndie.spwallet.sharedtheme.theme.spacing
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthedCardSheet(
    onDismiss: () -> Unit,
    cards: List<AuthedCardUi>,
    page: Int,
    onDeleteButtonClick: () -> Unit,
    onTransferButtonClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BaseSheetLayout(
        onDismiss = onDismiss
    ) {
        AuthedCardSheetContent(
            cards = cards,
            page = page,
            onDeleteButtonClick = onDeleteButtonClick,
            onTransferButtonClick = onTransferButtonClick,
            modifier = modifier
        )
    }
}

@Composable
private fun AuthedCardSheetContent(
    cards: List<AuthedCardUi>,
    page: Int,
    onDeleteButtonClick: () -> Unit,
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

            BaseTonalIconButton(
                icon = Icons.Outlined.People,
                text = stringResource(Res.string.transfer_by_number),
                onClick = { onTransferButtonClick(cards[page].id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium)
            )
        }

        BaseTextButton(
            text = stringResource(Res.string.deactivate),
            destructive = true,
            onClick = onDeleteButtonClick,
            modifier = Modifier
        )
    }
}