package com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.transferByCard

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.wynndie.spwallet.sharedCore.presentation.extensions.asColor
import com.wynndie.spwallet.sharedCore.presentation.extensions.asImage
import com.wynndie.spwallet.sharedCore.presentation.states.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.components.tiles.cards.TransferCardTile
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.by_number
import com.wynndie.spwallet.sharedResources.enter_comment
import com.wynndie.spwallet.sharedResources.enter_transfer_amount
import com.wynndie.spwallet.sharedResources.recipient
import com.wynndie.spwallet.sharedResources.transfer
import com.wynndie.spwallet.sharedResources.transfer_from
import com.wynndie.spwallet.sharedResources.transfer_to
import com.wynndie.spwallet.sharedtheme.designSystem.appBars.top.TopAppBar
import com.wynndie.spwallet.sharedtheme.designSystem.buttons.Button
import com.wynndie.spwallet.sharedtheme.designSystem.inputField.InputField
import com.wynndie.spwallet.sharedtheme.designSystem.lists.BaseCarousel
import com.wynndie.spwallet.sharedtheme.designSystem.loading.LoadingScreen
import com.wynndie.spwallet.sharedtheme.theme.spacing
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferByCardScreenRoot(
    viewModel: TransferByCardViewModel
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(Res.string.by_number),
                onClickBack = { viewModel.onAction(TransferByCardAction.OnClickBack) },
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
                    TransferByNumberScreen(
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
private fun TransferByNumberScreen(
    state: TransferByCardState,
    onAction: (TransferByCardAction) -> Unit,
    modifier: Modifier = Modifier
) {

    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            BaseCarousel(
                items = state.cards,
                page = state.carouselPage,
                onSwipePage = {
                    onAction(TransferByCardAction.OnSwipeCarousel(it))
                },
                modifier = Modifier
            ) { card ->
                Box(
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
                ) {
                    TransferCardTile(
                        title = stringResource(Res.string.transfer_from),
                        icon = card.icon.asImage(),
                        color = card.color.asColor(),
                        name = card.name,
                        number = card.number,
                        balance = card.balance,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            TransferCardTile(
                title = stringResource(Res.string.transfer_to),
                icon = state.recipient.icon.asImage(),
                color = if (state.recipient.name.isNotBlank()) {
                    state.recipient.color.asColor()
                } else MaterialTheme.colorScheme.tertiary,
                name = state.recipient.name.ifBlank { stringResource(Res.string.recipient) },
                number = state.recipient.number,
                onClick = { onAction(TransferByCardAction.OnClickRecipient) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium)
            )
        }

        Spacer(Modifier.height(MaterialTheme.spacing.large))

        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {

            InputField(
                value = state.amountInputFieldState.value,
                onValueChange = { onAction(TransferByCardAction.OnChangeTransferAmountValue(it)) },
                label = stringResource(Res.string.enter_transfer_amount),
                supportingText = state.amountInputFieldState.supportingText?.asString(),
                hasError = state.amountInputFieldState.hasError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
            )

            InputField(
                value = state.commentInputFieldState.value,
                onValueChange = { onAction(TransferByCardAction.OnChangeCommentValue(it)) },
                label = stringResource(Res.string.enter_comment),
                supportingText = state.commentInputFieldState.supportingText?.asString(),
                hasError = state.commentInputFieldState.hasError,
                singleLine = false,
                minLines = 3,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.clearFocus(true)
                        onAction(
                            TransferByCardAction.OnClickTransfer(
                                cardNumber = state.recipient.number,
                                transferAmount = state.amountInputFieldState.value.text,
                                comment = state.commentInputFieldState.value.text
                            )
                        )
                    }
                ),
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
            )
        }

        Spacer(Modifier.height(MaterialTheme.spacing.large))
        Spacer(Modifier.weight(1f))

        Button(
            text = stringResource(Res.string.transfer),
            onClick = {
                onAction(
                    TransferByCardAction.OnClickTransfer(
                        cardNumber = state.recipient.number,
                        transferAmount = state.amountInputFieldState.value.text,
                        comment = state.commentInputFieldState.value.text
                    )
                )
            },
            modifier = Modifier
                .padding(MaterialTheme.spacing.medium)
                .fillMaxWidth()
        )
    }
}