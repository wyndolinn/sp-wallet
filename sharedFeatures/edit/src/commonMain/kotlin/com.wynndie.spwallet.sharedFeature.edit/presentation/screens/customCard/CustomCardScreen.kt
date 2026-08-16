package com.wynndie.spwallet.sharedFeature.edit.presentation.screens.customCard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.balance
import com.wynndie.spwallet.sharedCore.card_name
import com.wynndie.spwallet.sharedCore.cash_account
import com.wynndie.spwallet.sharedCore.delete
import com.wynndie.spwallet.sharedCore.delete_card_description
import com.wynndie.spwallet.sharedCore.delete_card_title
import com.wynndie.spwallet.sharedCore.ic_delete
import com.wynndie.spwallet.sharedCore.presentation.components.TopAppBar
import com.wynndie.spwallet.sharedCore.presentation.components.buttons.Button
import com.wynndie.spwallet.sharedCore.presentation.components.inputField.InputField
import com.wynndie.spwallet.sharedCore.presentation.components.screen.Scaffold
import com.wynndie.spwallet.sharedCore.presentation.components.screen.ScreenLayout
import com.wynndie.spwallet.sharedCore.presentation.extensions.add
import com.wynndie.spwallet.sharedCore.presentation.extensions.asColor
import com.wynndie.spwallet.sharedCore.presentation.extensions.asPainter
import com.wynndie.spwallet.sharedCore.presentation.theme.AppTheme
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedCore.save
import com.wynndie.spwallet.sharedFeature.edit.presentation.components.CustomizableTile
import com.wynndie.spwallet.sharedFeature.edit.presentation.components.CustomizationSheet
import com.wynndie.spwallet.sharedFeature.edit.presentation.components.DeleteCardDialog
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomCardScreenRoot(
    viewModel: CustomCardViewModel,
    modifier: Modifier = Modifier
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
            title = stringResource(Res.string.delete_card_title),
            description = stringResource(Res.string.delete_card_description),
            onConfirm = { viewModel.onAction(CustomCardAction.DeleteCard) },
            onDismiss = { viewModel.onAction(CustomCardAction.ToggleDeleteDialog(false)) }
        )
    }

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
                }
            )
        },
        loadingState = state.screenLoadingState,
        focusManager = focusManager,
        modifier = modifier
    ) { innerPadding ->
        ScreenLayout(
            contentPadding = innerPadding.add(MaterialTheme.spacing.medium),
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            CustomCardScreen(
                state = state,
                onAction = viewModel::onAction,
                focusManager = focusManager,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun CustomCardScreen(
    state: CustomCardState,
    onAction: (CustomCardAction) -> Unit,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraLarge),
        modifier = modifier
    ) {
        CustomizableTile(
            color = state.card.color.asColor(),
            icon = state.card.icon.asPainter(),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = { onAction(CustomCardAction.ToggleCustomizationSheet(true)) }
                )
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
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
//                visualTransformation = AmountVisualTransformation(),
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.medium)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomCardScreenPreview() {
    AppTheme {
        CustomCardScreen(
            state = CustomCardState(),
            onAction = { },
            focusManager = LocalFocusManager.current,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(MaterialTheme.spacing.medium)
        )
    }
}