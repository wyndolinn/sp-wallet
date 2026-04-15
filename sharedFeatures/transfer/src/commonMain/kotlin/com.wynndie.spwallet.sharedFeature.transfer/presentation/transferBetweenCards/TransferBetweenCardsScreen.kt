package com.wynndie.spwallet.sharedFeature.transfer.presentation.transferBetweenCards

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wynndie.spwallet.sharedCore.presentation.components.BaseCarousel
import com.wynndie.spwallet.sharedCore.presentation.components.TopAppBar
import com.wynndie.spwallet.sharedCore.presentation.components.buttons.Button
import com.wynndie.spwallet.sharedCore.presentation.components.inputField.InputField
import com.wynndie.spwallet.sharedCore.presentation.components.loading.LoadingScreen
import com.wynndie.spwallet.sharedCore.presentation.components.tiles.TransferCardTile
import com.wynndie.spwallet.sharedCore.presentation.extensions.asColor
import com.wynndie.spwallet.sharedCore.presentation.extensions.asPainter
import com.wynndie.spwallet.sharedCore.presentation.formatters.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.extensions.asFormattedAmount
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.between_cards
import com.wynndie.spwallet.sharedResources.transfer_amount
import com.wynndie.spwallet.sharedResources.transfer
import com.wynndie.spwallet.sharedResources.transfer_from
import com.wynndie.spwallet.sharedResources.transfer_to
import com.wynndie.spwallet.sharedResources.x_of_ore
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferBetweenCardsScreenRoot(
    viewModel: TransferBetweenCardsViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(Res.string.between_cards),
                onClickBack = { viewModel.onAction(TransferBetweenCardsAction.NavigateBack) },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier
            .imePadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { focusManager.clearFocus(true) }
                )
            }
    ) { innerPadding ->

        Crossfade(
            targetState = state.loadingState,
            animationSpec = tween(500),
            modifier = Modifier.padding(innerPadding)
        ) { screenState ->

            when (screenState) {
                LoadingState.Loading -> {
                    LoadingScreen(modifier = Modifier.fillMaxSize())
                }

                is LoadingState.Failed -> {

                }

                LoadingState.Finished -> {
                    TransferBetweenCardsScreenContent(
                        state = state,
                        onAction = viewModel::onAction,
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    )
                }
            }
        }
    }
}

@Composable
private fun TransferBetweenCardsScreenContent(
    state: TransferBetweenCardsState,
    onAction: (TransferBetweenCardsAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraLarge),
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            BaseCarousel(
                items = state.sourceCards,
                page = state.selectedSourceCard,
                onSwipePage = { onAction(TransferBetweenCardsAction.SelectSourceCard(it)) },
                contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium),
                pageSpacing = MaterialTheme.spacing.medium
            ) { card ->
                TransferCardTile(
                    headline = stringResource(Res.string.transfer_from),
                    title = stringResource(Res.string.x_of_ore, card.balance)
                        .asFormattedAmount()
                        .uppercase(),
                    text = "${card.number} • ${card.name}",
                    icon = card.icon.asPainter(),
                    color = card.color.asColor(),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            BaseCarousel(
                items = state.destinationCards,
                page = state.selectedDestinationCard,
                onSwipePage = { onAction(TransferBetweenCardsAction.SelectDestinationCard(it)) },
                contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium),
                pageSpacing = MaterialTheme.spacing.medium
            ) { card ->
                if (card.balance == null) {
                    TransferCardTile(
                        headline = stringResource(Res.string.transfer_to),
                        title = card.name,
                        text = card.number,
                        icon = card.icon.asPainter(),
                        color = card.color.asColor(),
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    TransferCardTile(
                        headline = stringResource(Res.string.transfer_to),
                        title = stringResource(Res.string.x_of_ore, card.balance)
                            .asFormattedAmount().uppercase(),
                        text = "${card.number} • ${card.name}",
                        icon = card.icon.asPainter(),
                        color = card.color.asColor(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        InputField(
            value = state.amountInputFieldState.value,
            onValueChange = { onAction(TransferBetweenCardsAction.ChangeAmountValue(it)) },
            label = stringResource(Res.string.transfer_amount),
            supportingText = state.amountInputFieldState.supportingText?.asString(),
            hasError = state.amountInputFieldState.hasError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
        )

        Button(
            text = stringResource(Res.string.transfer),
            onClick = { onAction(TransferBetweenCardsAction.MakeTransfer) },
            enabled = state.isTransferButtonEnabled,
            modifier = Modifier
                .padding(MaterialTheme.spacing.medium)
                .fillMaxWidth()
        )
    }
}