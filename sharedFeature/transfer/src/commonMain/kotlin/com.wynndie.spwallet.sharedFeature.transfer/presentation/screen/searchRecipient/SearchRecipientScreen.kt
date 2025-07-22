package com.wynndie.spwallet.sharedFeature.transfer.presentation.screen.searchRecipient

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wynndie.spwallet.sharedCore.presentation.component.appDesignSystem.AppMenuTile
import com.wynndie.spwallet.sharedCore.presentation.component.baseDesignSystem.infoPanel.BaseInfoPanelLarge
import com.wynndie.spwallet.sharedCore.presentation.component.baseDesignSystem.inputField.BaseInputField
import com.wynndie.spwallet.sharedCore.presentation.model.card.CardColor
import com.wynndie.spwallet.sharedCore.presentation.model.card.CardIcon
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.empty_history
import com.wynndie.spwallet.sharedResources.enter_recipient_card_number
import com.wynndie.spwallet.sharedResources.recipient
import com.wynndie.spwallet.sharedResources.recipient_card
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
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { focusManager.clearFocus(true) }
                )
            }
    ) { innerPadding ->
        SearchRecipientScreenContent(
            state = state,
            onAction = viewModel::onAction,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun SearchRecipientScreenContent(
    state: SearchRecipientState,
    onAction: (SearchRecipientAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        BaseInputField(
            value = state.recipientInputFieldState.value,
            onValueChange = { onAction(SearchRecipientAction.OnChangeRecipientValue(it)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { onAction(SearchRecipientAction.OnClickRecipient(null, null)) }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        when {
            state.recipients.isNotEmpty() -> {
                LazyColumn {
                    items(state.recipients) { recipient ->
                        val recipientTile = recipient.asTile()
                        AppMenuTile(
                            icon = recipientTile.icon.value,
                            title = recipientTile.title,
                            description = recipientTile.description,
                            iconBackground = recipientTile.iconBackground.value,
                            trailingContent = {
                                IconButton(
                                    onClick = {
                                        onAction(
                                            SearchRecipientAction.OnClickEditRecipient(
                                                id = recipient.id
                                            )
                                        )
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Edit,
                                        contentDescription = null
                                    )
                                }
                            },
                            onClick = {
                                onAction(SearchRecipientAction.OnClickRecipient(recipientTile.id))
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            state.isNewRecipient -> {
                AppMenuTile(
                    icon = CardIcon.Person.value,
                    title = stringResource(Res.string.recipient_card),
                    description = state.recipientInputFieldState.value.text,
                    iconBackground = CardColor.Blue.value,
                    trailingContent = {
                        IconButton(
                            onClick = {
                                onAction(
                                    SearchRecipientAction.OnClickEditRecipient(
                                        cardNumber = state.recipientInputFieldState.value.text
                                    )
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Add,
                                contentDescription = null
                            )
                        }
                    },
                    onClick = { onAction(SearchRecipientAction.OnClickRecipient()) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            else -> {
                BaseInfoPanelLarge(
                    title = stringResource(Res.string.empty_history),
                    description = stringResource(Res.string.enter_recipient_card_number)
                )
            }
        }
    }
}