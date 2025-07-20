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
import com.wynndie.spwallet.sharedCore.presentation.component.baseDesignSystem.inputField.BaseInputField
import com.wynndie.spwallet.sharedCore.presentation.component.baseDesignSystem.inputField.TitledInputField
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.recipient
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

        LazyColumn {
            items(state.recipients) { recipient ->
                val recipientTile = recipient.asTile()
                AppMenuTile(
                    icon = recipientTile.icon.value,
                    title = recipientTile.title,
                    description = recipientTile.description,
                    iconBackground = recipientTile.iconBackground.value,
                    onClick = {
                        onAction(SearchRecipientAction.OnClickRecipient(recipientTile.id))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}