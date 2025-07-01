package com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.transfer_by_card

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.wynndie.spwallet.sharedCore.presentation.component.button.UiButton
import com.wynndie.spwallet.sharedCore.presentation.component.inputField.UiOutlinedInputField
import com.wynndie.spwallet.sharedCore.presentation.component.loading.LoadingScreen
import com.wynndie.spwallet.sharedCore.presentation.model.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedFeature.wallet.presentation.component.UiCardCarousel
import com.wynndie.spwallet.sharedFeature.wallet.presentation.component.UiCardItem
import com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.transfer_by_card.component.RecipientSheet
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.by_number
import com.wynndie.spwallet.sharedResources.comment
import com.wynndie.spwallet.sharedResources.enter_comment
import com.wynndie.spwallet.sharedResources.enter_transfer_amount
import com.wynndie.spwallet.sharedResources.transfer
import com.wynndie.spwallet.sharedResources.transfer_amount
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferByCardScreenRoot(
    viewModel: TransferByCardViewModel,
    navigateBack: () -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    if (state.isChangeRecipientSheetVisible) {
        RecipientSheet(
            onDismiss = {
                viewModel.onAction(TransferByCardAction.OnToggleRecipientSheet)
            },
            onClickRecipient = { viewModel.onAction(TransferByCardAction.OnClickRecipient(it)) },
            receiverInputFieldState = state.recipientInputFieldState,
            onChangeRecipientValue = {
                viewModel.onAction(TransferByCardAction.OnChangeRecipientValue(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium)
        )
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = { navigateBack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(Res.string.by_number),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier
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
                        navigateBack = navigateBack,
                        modifier = Modifier
                            .fillMaxSize()
                            .imePadding()
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
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            UiCardCarousel(
                items = state.cards,
                page = state.carouselPage,
                modifier = Modifier.fillMaxWidth()
            )

            UiCardItem(
                icon = state.recipient.icon.value,
                iconBackground = state.recipient.iconBackground.value,
                label = state.recipient.label.asString(),
                title = state.recipient.formatTitle().title.asString(),
                description = state.recipient.formatDescription().description?.asString(),
                onClick = { onAction(TransferByCardAction.OnToggleRecipientSheet) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium)
            )
        }

        Spacer(Modifier.height(MaterialTheme.spacing.large))

        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {

            UiOutlinedInputField(
                value = state.amountInputFieldState.value,
                onValueChange = { onAction(TransferByCardAction.OnChangeTransferAmountValue(it)) },
                label = stringResource(Res.string.enter_transfer_amount),
                placeholder = stringResource(Res.string.transfer_amount),
                supportingText = state.amountInputFieldState.supportingText.asString(),
                isError = state.amountInputFieldState.supportingText.asString().isNotBlank(),
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

            UiOutlinedInputField(
                value = state.commentInputFieldState.value,
                onValueChange = { onAction(TransferByCardAction.OnChangeCommentValue(it)) },
                label = stringResource(Res.string.enter_comment),
                placeholder = stringResource(Res.string.comment),
                suffix = state.commentInputFieldState.suffix?.asString(),
                supportingText = state.commentInputFieldState.supportingText.asString(),
                isError = state.commentInputFieldState.supportingText.asString().isNotBlank(),
                singleLine = false,
                minLines = 2,
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
                                comment = state.commentInputFieldState.value.text,
                                navigateBack = navigateBack
                            )
                        )
                    }
                ),
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
            )
        }

        Spacer(Modifier.height(MaterialTheme.spacing.large))
        Spacer(Modifier.weight(1f))

        UiButton(
            text = stringResource(Res.string.transfer),
            onClick = {
                onAction(
                    TransferByCardAction.OnClickTransfer(
                        cardNumber = state.recipient.number,
                        transferAmount = state.amountInputFieldState.value.text,
                        comment = state.commentInputFieldState.value.text,
                        navigateBack = navigateBack
                    )
                )
            },
            modifier = Modifier
                .padding(MaterialTheme.spacing.medium)
                .fillMaxWidth()
        )
    }
}