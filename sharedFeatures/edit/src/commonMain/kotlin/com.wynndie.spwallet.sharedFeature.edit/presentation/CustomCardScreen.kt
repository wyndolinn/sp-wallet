package com.wynndie.spwallet.sharedFeature.edit.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wynndie.spwallet.sharedCore.presentation.components.BalanceComponent
import com.wynndie.spwallet.sharedCore.presentation.components.TopAppBar
import com.wynndie.spwallet.sharedCore.presentation.components.buttons.Button
import com.wynndie.spwallet.sharedCore.presentation.components.inputField.InputField
import com.wynndie.spwallet.sharedCore.presentation.components.loading.LoadingScreen
import com.wynndie.spwallet.sharedCore.presentation.extensions.asColor
import com.wynndie.spwallet.sharedCore.presentation.extensions.asDisplayableOre
import com.wynndie.spwallet.sharedCore.presentation.extensions.asPainter
import com.wynndie.spwallet.sharedCore.presentation.formatters.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.theme.AppTheme
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedFeature.edit.presentation.components.CustomizableTile
import com.wynndie.spwallet.sharedFeature.edit.presentation.components.CustomizationSheet
import com.wynndie.spwallet.sharedFeature.edit.presentation.components.DeleteCardDialog
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.balance
import com.wynndie.spwallet.sharedResources.card_name
import com.wynndie.spwallet.sharedResources.cash_account
import com.wynndie.spwallet.sharedResources.delete
import com.wynndie.spwallet.sharedResources.ic_delete
import com.wynndie.spwallet.sharedResources.save
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomCardScreenRoot(
    viewModel: CustomCardViewModel
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    if (state.isCustomizationSheetVisible) {
        CustomizationSheet(
            onDismiss = { viewModel.onAction(CustomCardAction.ToggleCustomizationSheet(false)) },
            selectedColor = state.selectedColorChip,
            onColorClick = { viewModel.onAction(CustomCardAction.SelectColor(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium)
        )
    }

    if (state.isDeleteDialogVisible) {
        DeleteCardDialog(
            onConfirm = { viewModel.onAction(CustomCardAction.DeleteCard) },
            onDismiss = { viewModel.onAction(CustomCardAction.ToggleDeleteDialog(false)) },
            modifier = Modifier
        )
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(Res.string.cash_account),
                onClickBack = { viewModel.onAction(CustomCardAction.NavigateBack) },
                actions = {
                    if (state.card.id.isNotBlank()) {
                        IconButton(
                            onClick = { viewModel.onAction(CustomCardAction.ToggleDeleteDialog(true)) }
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_delete),
                                contentDescription = stringResource(Res.string.delete),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                },
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
            targetState = state.screenLoadingState,
            animationSpec = tween(500),
            modifier = Modifier
                .padding(innerPadding)
                .padding(MaterialTheme.spacing.medium)
        ) { screenState ->

            when (screenState) {
                LoadingState.Loading -> {
                    LoadingScreen(modifier = Modifier.fillMaxSize())
                }

                is LoadingState.Failed -> {

                }

                LoadingState.Finished -> {
                    CustomCardScreen(
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
private fun CustomCardScreen(
    state: CustomCardState,
    onAction: (CustomCardAction) -> Unit,
    modifier: Modifier = Modifier
) {

    val focusManager = LocalFocusManager.current

    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraLarge),
        modifier = modifier
    ) {

        BalanceComponent(
            title = stringResource(Res.string.balance),
            balance = state.card.balance.asDisplayableOre()
        )

        CustomizableTile(
            color = state.card.color.asColor(),
            icon = state.card.icon.asPainter(),
            onClick = { onAction(CustomCardAction.ToggleCustomizationSheet(true)) },
            modifier = Modifier.fillMaxWidth()
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
        ) {
            InputField(
                value = state.nameInputFieldState.value,
                onValueChange = { onAction(CustomCardAction.ChangeNameValue(it)) },
                label = stringResource(Res.string.card_name),
                supportingText = state.nameInputFieldState.supportingText?.asString(),
                hasError = state.nameInputFieldState.hasError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )

            InputField(
                value = state.balanceInputFieldState.value,
                onValueChange = { onAction(CustomCardAction.ChangeBalanceValue(it)) },
                label = stringResource(Res.string.balance),
                supportingText = state.balanceInputFieldState.supportingText?.asString(),
                hasError = state.balanceInputFieldState.hasError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus(true)
                    }
                )
            )
        }

        Button(
            text = stringResource(Res.string.save),
            onClick = { onAction(CustomCardAction.SaveCard) },
            enabled = state.isSaveButtonEnabled,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomCardScreenPreview() {
    AppTheme {
        CustomCardScreen(
            state = CustomCardState(),
            onAction = { _ -> },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(MaterialTheme.spacing.medium)
        )
    }
}