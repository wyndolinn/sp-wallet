package com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.searchRecipient

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardIcons
import com.wynndie.spwallet.sharedCore.presentation.components.tiles.TransparentTile
import com.wynndie.spwallet.sharedCore.presentation.extensions.asColor
import com.wynndie.spwallet.sharedCore.presentation.extensions.asImage
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.enter_recipient_card_number
import com.wynndie.spwallet.sharedResources.recipient
import com.wynndie.spwallet.sharedResources.recipient_card
import com.wynndie.spwallet.sharedResources.recipient_history_empty
import com.wynndie.spwallet.sharedtheme.designSystem.buttons.BaseIconButton
import com.wynndie.spwallet.sharedtheme.designSystem.infoLayouts.vertical.BaseInfoPanelSmall
import com.wynndie.spwallet.sharedtheme.designSystem.inputField.BaseInputField
import com.wynndie.spwallet.sharedtheme.theme.spacing
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
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.onAction(SearchRecipientAction.OnClickBack) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(Res.string.recipient),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
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
        BaseInputField(
            value = state.recipientInputField.value,
            onValueChange = { onAction(SearchRecipientAction.OnChangeRecipientValue(it)) },
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
                LazyColumn {
                    items(state.recipients) { recipient ->
                        TransparentTile(
                            icon = recipient.icon.asImage(),
                            title = recipient.name,
                            description = recipient.number,
                            iconBackground = recipient.color.asColor(),
                            trailingContent = {
                                BaseIconButton(
                                    icon = Icons.Outlined.Edit,
                                    onClick = {
                                        onAction(
                                            SearchRecipientAction.OnClickEditRecipient(recipient.id)
                                        )
                                    }
                                )
                            },
                            onClick = {
                                onAction(SearchRecipientAction.OnClickRecipient(recipient.id))
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            state.isNewRecipient -> {
                TransparentTile(
                    icon = CardIcons.PERSON.asImage(),
                    title = stringResource(Res.string.recipient_card),
                    description = state.recipientInputField.value.text,
                    iconBackground = CardColors.BLUE.asColor(),
                    trailingContent = {
                        BaseIconButton(
                            icon = Icons.Outlined.Add,
                            onClick = {
                                onAction(
                                    SearchRecipientAction.OnClickEditRecipient(
                                        cardNumber = state.recipientInputField.value.text
                                    )
                                )
                            }
                        )
                    },
                    onClick = {
                        onAction(
                            SearchRecipientAction.OnClickRecipient(
                                cardNumber = state.recipientInputField.value.text
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = MaterialTheme.spacing.medium)
                )
            }

            else -> {
                BaseInfoPanelSmall(
                    title = stringResource(Res.string.recipient_history_empty),
                    description = stringResource(Res.string.enter_recipient_card_number),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.medium)
                )
            }
        }
    }
}