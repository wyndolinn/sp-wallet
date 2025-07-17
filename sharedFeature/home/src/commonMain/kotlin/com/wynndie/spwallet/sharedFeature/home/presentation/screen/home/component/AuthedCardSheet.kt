package com.wynndie.spwallet.sharedFeature.home.presentation.screen.home.component

import androidx.compose.foundation.layout.Arrangement
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
import com.wynndie.spwallet.sharedCore.presentation.component.designSystem.button.UiTextButton
import com.wynndie.spwallet.sharedCore.presentation.component.designSystem.button.UiTonalIconButton
import com.wynndie.spwallet.sharedCore.presentation.component.designSystem.dialog.BottomSheetScaffold
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedCore.presentation.component.tile.UiCardCarousel
import com.wynndie.spwallet.sharedCore.presentation.model.UiAuthedCard
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.deactivate
import com.wynndie.spwallet.sharedResources.transfer_by_number
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthedCardSheet(
    onDismiss: () -> Unit,
    cards: List<UiAuthedCard>,
    page: Int,
    onDeleteButtonClick: () -> Unit,
    onTransferButtonClick: (UiAuthedCard) -> Unit,
    modifier: Modifier = Modifier
) {
    BottomSheetScaffold(
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
    cards: List<UiAuthedCard>,
    page: Int,
    onDeleteButtonClick: () -> Unit,
    onTransferButtonClick: (UiAuthedCard) -> Unit,
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
            UiCardCarousel(
                items = cards,
                page = page,
                modifier = Modifier.fillMaxWidth()
            )

            UiTonalIconButton(
                icon = Icons.Outlined.People,
                text = stringResource(Res.string.transfer_by_number),
                onClick = { onTransferButtonClick(cards[page]) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium)
            )
        }

        UiTextButton(
            text = stringResource(Res.string.deactivate),
            destructive = true,
            onClick = onDeleteButtonClick,
            modifier = Modifier
        )
    }
}