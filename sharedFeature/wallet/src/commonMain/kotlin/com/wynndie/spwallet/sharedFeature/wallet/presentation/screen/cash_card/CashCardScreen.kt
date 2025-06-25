package com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.cash_card

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wynndie.spwallet.sharedCore.presentation.component.button.UiButton
import com.wynndie.spwallet.sharedCore.presentation.component.infoDisplay.MediumInfoDisplay
import com.wynndie.spwallet.sharedCore.presentation.component.inputField.UiOutlinedInputField
import com.wynndie.spwallet.sharedCore.presentation.component.loading.LoadingScreen
import com.wynndie.spwallet.sharedCore.presentation.mapper.joinAsString
import com.wynndie.spwallet.sharedCore.presentation.model.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.theme.AppTheme
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.cash_card.component.CustomizableCard
import com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.cash_card.component.CustomizationSheet
import com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.cash_card.component.DeleteCardDialog
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.balance
import com.wynndie.spwallet.sharedResources.cash_account
import com.wynndie.spwallet.sharedResources.delete
import com.wynndie.spwallet.sharedResources.enter_balance
import com.wynndie.spwallet.sharedResources.no_name
import com.wynndie.spwallet.sharedResources.save
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CashCardScreenRoot(
    viewModel: CashCardViewModel,
    navigateBack: () -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    if (state.isCustomizationSheetVisible) {
        CustomizationSheet(
            onDismiss = { viewModel.onAction(CashCardAction.OnToggleCustomizationSheet) },
            idInputField = state.nameInputField,
            onIdValueChange = { viewModel.onAction(CashCardAction.OnChangeNameValue(it)) },
            selectedColorChip = state.selectedColorChip,
            onColorChipClick = { viewModel.onAction(CashCardAction.OnClickColorChip(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium)
        )
    }

    if (state.isDeleteDialogVisible) {
        DeleteCardDialog(
            onConfirm = { viewModel.onAction(CashCardAction.OnClickDeleteCard(navigateBack)) },
            onDismiss = { viewModel.onAction(CashCardAction.OnToggleDeleteDialog) },
            modifier = Modifier
        )
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = navigateBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(Res.string.cash_account),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                actions = {
                    if (state.card.id.isNotBlank()) {
                        IconButton(
                            onClick = { viewModel.onAction(CashCardAction.OnToggleDeleteDialog) }
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
                    CashCardScreen(
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
private fun CashCardScreen(
    state: CashCardState,
    onAction: (CashCardAction) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
    ) {

        MediumInfoDisplay(
            label = state.card.name.ifBlank {
                stringResource(Res.string.no_name)
            },
            title = state.card.title.asString(),
            description = state.card.balance.formatted.joinAsString(" "),
            modifier = Modifier
        )

        Spacer(Modifier.height(MaterialTheme.spacing.large))

        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            CustomizableCard(
                card = state.card,
                onClick = { onAction(CashCardAction.OnToggleCustomizationSheet) },
                modifier = Modifier.fillMaxWidth()
            )

            UiOutlinedInputField(
                value = state.balanceInputField.value,
                onValueChange = { onAction(CashCardAction.OnChangeBalanceValue(it)) },
                label = stringResource(Res.string.enter_balance),
                placeholder = stringResource(Res.string.balance),
                supportingText = state.balanceInputField.supportingText.asString(),
                isError = state.balanceInputField.supportingText.asString().isNotBlank(),
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

        UiButton(
            text = stringResource(Res.string.save),
            onClick = {
                onAction(
                    CashCardAction.OnClickSaveCard(
                        cardName = state.nameInputField.value.text,
                        cardBalance = state.balanceInputField.value.text,
                        navigateBack = navigateBack
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun CashCardScreenPreview() {
    AppTheme {
        CashCardScreen(
            state = CashCardState(),
            onAction = { _ -> },
            navigateBack = {  },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(MaterialTheme.spacing.medium)
        )
    }
}