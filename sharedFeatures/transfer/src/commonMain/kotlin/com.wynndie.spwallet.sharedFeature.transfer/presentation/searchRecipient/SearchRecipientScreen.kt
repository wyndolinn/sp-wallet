package com.wynndie.spwallet.sharedFeature.transfer.presentation.searchRecipient

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wynndie.spwallet.sharedCore.presentation.components.TopAppBar
import com.wynndie.spwallet.sharedCore.presentation.components.inputField.InputField
import com.wynndie.spwallet.sharedCore.presentation.components.tiles.RecipientTile
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.card_number
import com.wynndie.spwallet.sharedResources.enter_recipient_card_number
import com.wynndie.spwallet.sharedResources.recipient
import com.wynndie.spwallet.sharedResources.recipient_history_empty
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchRecipientScreenRoot(
    viewModel: SearchRecipientViewModel
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(Res.string.recipient),
                onClickBack = { viewModel.onAction(SearchRecipientAction.NavigateBack) }
            )
        },
        modifier = Modifier
            .systemBarsPadding()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { focusManager.clearFocus(true) }
                )
            }
    ) { innerPadding ->
        SearchRecipientScreenContent(
            state = state,
            onAction = viewModel::onAction,
            focusManager = focusManager,
            modifier = Modifier
                .padding(innerPadding)
                .imePadding()
        )
    }
}

@Composable
private fun SearchRecipientScreenContent(
    state: SearchRecipientState,
    onAction: (SearchRecipientAction) -> Unit,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        InputField(
            value = state.recipientInputFieldState.value,
            onValueChange = { onAction(SearchRecipientAction.ChangeRecipientValue(it)) },
            placeholder = stringResource(Res.string.card_number),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
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
                    contentPadding = PaddingValues(vertical = MaterialTheme.spacing.extraLarge)
                ) {
                    items(state.recipients) { recipient ->
                        RecipientTile(
                            label = stringResource(Res.string.recipient),
                            title = recipient.number,
                            onClick = {
                                onAction(SearchRecipientAction.SelectRecipient(recipient.number))
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            state.isNewRecipient -> {
                val cardNumber = state.recipientInputFieldState.value.text
                RecipientTile(
                    label = stringResource(Res.string.recipient),
                    title = cardNumber,
                    onClick = {
                        onAction(SearchRecipientAction.SelectRecipient(cardNumber))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = MaterialTheme.spacing.extraLarge)
                )
            }

            else -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraExtraSmall),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
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
    }
}