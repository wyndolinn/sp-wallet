package com.wynndie.spwallet.sharedFeature.edit.presentation.screens.customCard

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wynndie.spwallet.sharedCore.presentation.components.balance.BalanceComponent
import com.wynndie.spwallet.sharedCore.presentation.extensions.asColor
import com.wynndie.spwallet.sharedCore.presentation.extensions.asImage
import com.wynndie.spwallet.sharedCore.presentation.states.LoadingState
import com.wynndie.spwallet.sharedFeature.edit.presentation.components.CustomizableTile
import com.wynndie.spwallet.sharedFeature.edit.presentation.components.CustomizationSheet
import com.wynndie.spwallet.sharedFeature.edit.presentation.screens.customCard.component.DeleteCardDialog
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.balance
import com.wynndie.spwallet.sharedResources.card_name
import com.wynndie.spwallet.sharedResources.cash_account
import com.wynndie.spwallet.sharedResources.delete
import com.wynndie.spwallet.sharedResources.enter_balance
import com.wynndie.spwallet.sharedResources.enter_card_name
import com.wynndie.spwallet.sharedResources.no_name
import com.wynndie.spwallet.sharedResources.save
import com.wynndie.spwallet.sharedtheme.designSystem.appBars.top.TopAppBar
import com.wynndie.spwallet.sharedtheme.designSystem.buttons.Button
import com.wynndie.spwallet.sharedtheme.designSystem.inputField.InputField
import com.wynndie.spwallet.sharedtheme.designSystem.loading.LoadingScreen
import com.wynndie.spwallet.sharedtheme.theme.AppTheme
import com.wynndie.spwallet.sharedtheme.theme.spacing
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomCardScreenRoot(
    viewModel: CustomCardViewModel
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    if (state.isCustomizationSheetVisible) {
        CustomizationSheet(
            onDismiss = { viewModel.onAction(CustomCardAction.OnToggleCustomizationSheet) },
            selectedColorChip = state.selectedColorChip,
            onColorChipClick = { viewModel.onAction(CustomCardAction.OnClickColorChip(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium)
        )
    }

    if (state.isDeleteDialogVisible) {
        DeleteCardDialog(
            onConfirm = { viewModel.onAction(CustomCardAction.OnClickDeleteCard) },
            onDismiss = { viewModel.onAction(CustomCardAction.OnToggleDeleteDialog) },
            modifier = Modifier
        )
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(Res.string.cash_account),
                onClickBack = { viewModel.onAction(CustomCardAction.OnClickBack) },
                actions = {
                    if (state.card.id.isNotBlank()) {
                        IconButton(
                            onClick = { viewModel.onAction(CustomCardAction.OnToggleDeleteDialog) }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
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
        modifier = modifier
    ) {

        BalanceComponent(
            title = state.card.name.ifBlank { stringResource(Res.string.no_name) },
            balance = state.card.balance
        )

        Spacer(Modifier.height(MaterialTheme.spacing.large))

        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            CustomizableTile(
                color = state.card.color.asColor(),
                icon = state.card.icon.asImage(),
                onClick = { onAction(CustomCardAction.OnToggleCustomizationSheet) },
                modifier = Modifier.fillMaxWidth()
            )

            InputField(
                value = state.nameInputFieldState.value,
                onValueChange = { onAction(CustomCardAction.OnChangeNameValue(it)) },
                label = stringResource(Res.string.enter_card_name),
                placeholder = stringResource(Res.string.card_name),
                supportingText = state.nameInputFieldState.supportingText?.asString(),
                hasError = state.nameInputFieldState.hasError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus(true)
                    }
                )
            )

            InputField(
                value = state.balanceInputFieldState.value,
                onValueChange = { onAction(CustomCardAction.OnChangeBalanceValue(it)) },
                label = stringResource(Res.string.enter_balance),
                placeholder = stringResource(Res.string.balance),
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

        Spacer(Modifier.height(MaterialTheme.spacing.large))
        Spacer(Modifier.weight(1f))

        Button(
            text = stringResource(Res.string.save),
            onClick = {
                onAction(
                    CustomCardAction.OnClickSaveCard(
                        cardName = state.nameInputFieldState.value.text,
                        cardBalance = state.balanceInputFieldState.value.text
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
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