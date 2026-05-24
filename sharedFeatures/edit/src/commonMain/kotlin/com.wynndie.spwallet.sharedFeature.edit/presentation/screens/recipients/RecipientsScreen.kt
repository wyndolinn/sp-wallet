package com.wynndie.spwallet.sharedFeature.edit.presentation.screens.recipients

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.add
import com.wynndie.spwallet.sharedCore.card_number
import com.wynndie.spwallet.sharedCore.delete_recipient_description
import com.wynndie.spwallet.sharedCore.delete_recipient_title
import com.wynndie.spwallet.sharedCore.enter_recipient_card_number
import com.wynndie.spwallet.sharedCore.ic_edit
import com.wynndie.spwallet.sharedCore.presentation.components.TopAppBar
import com.wynndie.spwallet.sharedCore.presentation.components.buttons.OutlinedButton
import com.wynndie.spwallet.sharedCore.presentation.components.inputField.InputField
import com.wynndie.spwallet.sharedCore.presentation.components.tiles.RecipientTile
import com.wynndie.spwallet.sharedCore.presentation.theme.AppTheme
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedCore.recipient_history_empty
import com.wynndie.spwallet.sharedFeature.edit.presentation.components.DeleteCardDialog
import com.wynndie.spwallet.sharedFeature.edit.presentation.screens.recipients.components.EditRecipientSheet
import com.wynndie.spwallet.sharedFeature.edit.presentation.screens.recipients.components.RecipientSheet
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipientsScreenRoot(
    viewModel: RecipientsViewModel,
    modifier: Modifier = Modifier
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    val recipient = state.selectedRecipient
    if (recipient != null) {
        RecipientSheet(
            onDismiss = { viewModel.onAction(RecipientsAction.SelectRecipient(null)) },
            recipient = recipient,
            onDeleteButtonClick = { viewModel.onAction(RecipientsAction.DeleteRecipient) },
            onEditRecipientClick = {
                viewModel.onAction(RecipientsAction.ToggleEditRecipientSheet(true))
            },
            onTransferClick = { viewModel.onAction(RecipientsAction.MakeTransfer) },
            modifier = Modifier.padding(MaterialTheme.spacing.medium)
        )
    }

    if (state.isEditRecipientSheetOpen) {
        EditRecipientSheet(
            onDismiss = { viewModel.onAction(RecipientsAction.ToggleEditRecipientSheet(false)) },
            nameFieldState = state.cardNameInputFieldState,
            onNameChange = { viewModel.onAction(RecipientsAction.ChangeCardNameValue(it)) },
            onClearNameFocus = { viewModel.onAction(RecipientsAction.ClearCardNameFocus) },
            numberFieldState = state.cardNumberInputFieldState,
            onNumberChange = { viewModel.onAction(RecipientsAction.ChangeCardNumberValue(it)) },
            onClearNumberFocus = { viewModel.onAction(RecipientsAction.ClearCardNumberFocus) },
            isSaveButtonEnabled = state.isSaveButtonEnabled,
            onSaveRecipient = { viewModel.onAction(RecipientsAction.SaveRecipient) },
            modifier = Modifier.padding(MaterialTheme.spacing.medium)
        )
    }

    if (state.isDeleteDialogOpen) {
        DeleteCardDialog(
            title = stringResource(Res.string.delete_recipient_title),
            description = stringResource(Res.string.delete_recipient_description),
            onConfirm = { viewModel.onAction(RecipientsAction.DeleteRecipient) },
            onDismiss = { viewModel.onAction(RecipientsAction.ToggleDeleteRecipientDialog(false)) },
            modifier = Modifier
        )
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = "Получатели",
                onClickBack = { viewModel.onAction(RecipientsAction.NavigateBack) }
            )
        },
        modifier = modifier
            .imePadding()
            .pointerInput(Unit) {
                detectTapGestures { focusManager.clearFocus(true) }
            }
    ) { innerPadding ->
        RecipientsScreen(
            state = state,
            onAction = viewModel::onAction,
            focusManager = focusManager,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

@Composable
private fun RecipientsScreen(
    state: RecipientsState,
    onAction: (RecipientsAction) -> Unit,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        InputField(
            value = state.recipientInputFieldState.value,
            onValueChange = { onAction(RecipientsAction.ChangeRecipientValue(it)) },
            placeholder = stringResource(Res.string.card_number),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus(true) }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.medium)
        )

        when {
            state.recipients.isNotEmpty() -> {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = MaterialTheme.spacing.extraLarge),
                    modifier = Modifier.weight(1f)
                ) {
                    items(state.recipients) { recipient ->
                        RecipientTile(
                            label = recipient.name,
                            title = recipient.number,
                            actionIcon = painterResource(Res.drawable.ic_edit),
                            onClick = { onAction(RecipientsAction.SelectRecipient(recipient)) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            else -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraExtraSmall),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(
                            horizontal = MaterialTheme.spacing.medium,
                            vertical = MaterialTheme.spacing.extraLarge
                        )
                ) {
                    Text(
                        text = stringResource(Res.string.enter_recipient_card_number),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = stringResource(Res.string.recipient_history_empty),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        OutlinedButton(
            text = stringResource(Res.string.add),
            onClick = { onAction(RecipientsAction.ToggleEditRecipientSheet(true)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.medium)
                .padding(bottom = MaterialTheme.spacing.medium)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecipientsScreenPreview() {
    AppTheme {
        RecipientsScreen(
            state = RecipientsState(),
            onAction = { },
            focusManager = LocalFocusManager.current,
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.medium)
        )
    }
}