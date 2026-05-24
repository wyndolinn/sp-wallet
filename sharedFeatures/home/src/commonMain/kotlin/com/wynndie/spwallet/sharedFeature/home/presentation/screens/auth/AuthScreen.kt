package com.wynndie.spwallet.sharedFeature.home.presentation.screens.auth

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.activate
import com.wynndie.spwallet.sharedCore.id
import com.wynndie.spwallet.sharedCore.presentation.components.BaseCarousel
import com.wynndie.spwallet.sharedCore.presentation.components.TopAppBar
import com.wynndie.spwallet.sharedCore.presentation.components.buttons.Button
import com.wynndie.spwallet.sharedCore.presentation.components.buttons.TextButton
import com.wynndie.spwallet.sharedCore.presentation.components.inputField.InputField
import com.wynndie.spwallet.sharedCore.presentation.components.loading.LoadingDialog
import com.wynndie.spwallet.sharedCore.presentation.components.tiles.TransferCardTile
import com.wynndie.spwallet.sharedCore.presentation.extensions.asColor
import com.wynndie.spwallet.sharedCore.presentation.extensions.asPainter
import com.wynndie.spwallet.sharedCore.presentation.formatters.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.theme.AppTheme
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedCore.token
import com.wynndie.spwallet.sharedCore.presentation.components.InformationCard
import com.wynndie.spwallet.sharedCore.safe_auth
import com.wynndie.spwallet.sharedCore.safe_auth_reset
import com.wynndie.spwallet.sharedCore.safe_auth_storage
import com.wynndie.spwallet.sharedFeature.home.presentation.screens.auth.components.AuthHelpSheet
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreenRoot(
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    if (state.loadingState is LoadingState.Loading) {
        LoadingDialog()
    }

    if (state.isHelpSheetOpen) {
        AuthHelpSheet(
            onDismiss = { viewModel.onAction(AuthAction.ToggleHelpSheet(false)) },
            modifier = Modifier.padding(MaterialTheme.spacing.medium)
        )
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        topBar = {
            TopAppBar(
                title = "Активация",
                onClickBack = { viewModel.onAction(AuthAction.NavigateBack) },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = modifier
            .imePadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .pointerInput(Unit) {
                detectTapGestures { focusManager.clearFocus(true) }
            }
    ) { innerPadding ->
        AuthScreen(
            state = state,
            onAction = viewModel::onAction,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(MaterialTheme.spacing.medium)
        )
    }
}

@Composable
private fun AuthScreen(
    state: AuthState,
    onAction: (AuthAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    var currentPage by remember { mutableStateOf(0) }

    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraLarge),
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures { focusManager.clearFocus(true) }
        }
    ) {
        InformationCard(
            title = stringResource(Res.string.safe_auth),
            content = {
                Text(text = stringResource(Res.string.safe_auth_storage))
                Text(text = stringResource(Res.string.safe_auth_reset))
            },
            modifier = Modifier.fillMaxWidth()
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
        ) {
            if (state.unauthedCards.isNotEmpty()) {
                BaseCarousel(
                    items = state.unauthedCards,
                    page = currentPage,
                    onSwipePage = {
                        currentPage = it
                        val id = TextFieldValue(state.unauthedCards[it].id)
                        onAction(AuthAction.ChangeIdValue(id))
                    },
                    contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium),
                    pageSpacing = MaterialTheme.spacing.medium
                ) { card ->
                    TransferCardTile(
                        headline = stringResource(Res.string.activate),
                        title = card.name,
                        text = card.number,
                        icon = card.icon.asPainter(),
                        color = card.color.asColor(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                InputField(
                    value = state.idInputFieldState.value,
                    onValueChange = { onAction(AuthAction.ChangeIdValue(it)) },
                    label = stringResource(Res.string.id),
                    supportingText = state.idInputFieldState.supportingText?.asString(),
                    hasError = state.idInputFieldState.hasError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    modifier = Modifier.onFocusChanged {
                        if (!it.isFocused) onAction(AuthAction.ClearIdFocus)
                    }
                )
            }

            InputField(
                value = state.tokenInputFieldState.value,
                onValueChange = { onAction(AuthAction.ChangeTokenValue(it)) },
                label = stringResource(Res.string.token),
                supportingText = state.tokenInputFieldState.supportingText?.asString(),
                hasError = state.tokenInputFieldState.hasError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus(true)
                        val cardId = if (state.unauthedCards.isEmpty()) {
                            state.idInputFieldState.value.text
                        } else state.unauthedCards[currentPage].id
                        val token = state.tokenInputFieldState.value.text

                        onAction(AuthAction.AuthCard(cardId, token))
                    }
                ),
                modifier = Modifier.onFocusChanged {
                    if (!it.isFocused) onAction(AuthAction.ClearTokenFocus)
                }
            )

            TextButton(
                text = "Показать инструкцию",
                onClick = { onAction(AuthAction.ToggleHelpSheet(true)) }
            )
        }

        Button(
            text = stringResource(Res.string.activate),
            onClick = {
                val cardId = if (state.unauthedCards.isEmpty()) {
                    state.idInputFieldState.value.text
                } else state.unauthedCards[currentPage].id
                val token = state.tokenInputFieldState.value.text

                onAction(AuthAction.AuthCard(cardId, token))
            },
            enabled = state.isAuthButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthScreenPreview() {
    AppTheme {
        AuthScreen(
            state = AuthState(),
            onAction = { },
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.medium)
        )
    }
}