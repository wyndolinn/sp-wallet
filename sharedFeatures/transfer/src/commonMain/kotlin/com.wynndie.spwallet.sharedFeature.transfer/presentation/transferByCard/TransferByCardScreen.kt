package com.wynndie.spwallet.sharedFeature.transfer.presentation.transferByCard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.by_number
import com.wynndie.spwallet.sharedCore.comment
import com.wynndie.spwallet.sharedCore.presentation.components.BaseCarousel
import com.wynndie.spwallet.sharedCore.presentation.components.TopAppBar
import com.wynndie.spwallet.sharedCore.presentation.components.buttons.Button
import com.wynndie.spwallet.sharedCore.presentation.components.inputField.InputField
import com.wynndie.spwallet.sharedCore.presentation.components.screen.Scaffold
import com.wynndie.spwallet.sharedCore.presentation.components.screen.ScreenLayout
import com.wynndie.spwallet.sharedCore.presentation.components.tiles.TransferCardTile
import com.wynndie.spwallet.sharedCore.presentation.extensions.add
import com.wynndie.spwallet.sharedCore.presentation.extensions.asColor
import com.wynndie.spwallet.sharedCore.presentation.extensions.asPainter
import com.wynndie.spwallet.sharedCore.presentation.formatters.asFormattedAmount
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedCore.recipient
import com.wynndie.spwallet.sharedCore.transfer
import com.wynndie.spwallet.sharedCore.transfer_amount
import com.wynndie.spwallet.sharedCore.transfer_from
import com.wynndie.spwallet.sharedCore.transfer_to
import com.wynndie.spwallet.sharedCore.x_of_ore
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferByCardScreenRoot(
    viewModel: TransferByCardViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(Res.string.by_number),
                onClickBack = { viewModel.onAction(TransferByCardAction.NavigateBack) }
            )
        },
        loadingState = state.loadingState,
        focusManager = focusManager,
        modifier = modifier
    ) { innerPadding ->
        ScreenLayout(
            contentPadding = innerPadding.add(MaterialTheme.spacing.medium),
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            TransferByNumberScreen(
                state = state,
                onAction = viewModel::onAction,
                focusManager = focusManager,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun TransferByNumberScreen(
    state: TransferByCardState,
    onAction: (TransferByCardAction) -> Unit,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {
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
                onSwipePage = { onAction(TransferByCardAction.SelectSourceCard(it)) },
                contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium),
                pageSpacing = MaterialTheme.spacing.medium,
            ) { card ->
                TransferCardTile(
                    headline = stringResource(Res.string.transfer_from),
                    title = stringResource(Res.string.x_of_ore, card.balance)
                        .asFormattedAmount().uppercase(),
                    text = "${card.number} • ${card.name}",
                    icon = card.icon.asPainter(),
                    color = card.color.asColor(),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            TransferCardTile(
                headline = stringResource(Res.string.transfer_to),
                title = state.recipient.number,
                text = state.recipient.name.ifBlank { stringResource(Res.string.recipient) },
                icon = state.recipient.icon.asPainter(),
                color = if (state.recipient.name.isNotBlank()) {
                    state.recipient.color.asColor()
                } else MaterialTheme.colorScheme.tertiary,
                onClick = { onAction(TransferByCardAction.EditRecipient) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium)
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
        ) {
            InputField(
                value = state.amountInputFieldState.value,
                onValueChange = { onAction(TransferByCardAction.ChangeAmountValue(it)) },
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

            InputField(
                value = state.commentInputFieldState.value,
                onValueChange = { onAction(TransferByCardAction.ChangeCommentValue(it)) },
                label = stringResource(Res.string.comment),
                prefix = state.commentPrefix,
                placeholder = "Без комментария",
                supportingText = state.commentInputFieldState.supportingText?.asString(),
                hasError = state.commentInputFieldState.hasError,
                singleLine = false,
                minLines = 3,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus(true)
                    }
                ),
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
            )
        }

        Button(
            text = stringResource(Res.string.transfer),
            onClick = { onAction(TransferByCardAction.MakeTransfer) },
            enabled = state.isTransferButtonEnabled,
            modifier = Modifier
                .padding(MaterialTheme.spacing.medium)
                .fillMaxWidth()
        )
    }
}