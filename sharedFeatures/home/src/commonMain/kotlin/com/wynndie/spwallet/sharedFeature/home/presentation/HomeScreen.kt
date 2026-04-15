package com.wynndie.spwallet.sharedFeature.home.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wynndie.spwallet.sharedCore.domain.constructors.createAuthedCard
import com.wynndie.spwallet.sharedCore.domain.models.SpServers
import com.wynndie.spwallet.sharedCore.presentation.components.BalanceComponent
import com.wynndie.spwallet.sharedCore.presentation.components.TitledContent
import com.wynndie.spwallet.sharedCore.presentation.components.TopAppBar
import com.wynndie.spwallet.sharedCore.presentation.components.buttons.IconButton
import com.wynndie.spwallet.sharedCore.presentation.components.buttons.SegmentedButton
import com.wynndie.spwallet.sharedCore.presentation.components.buttons.TonalButton
import com.wynndie.spwallet.sharedCore.presentation.components.loading.LoadingScreen
import com.wynndie.spwallet.sharedCore.presentation.components.overlays.Dialog
import com.wynndie.spwallet.sharedCore.presentation.components.tiles.AccountCardTile
import com.wynndie.spwallet.sharedCore.presentation.extensions.asColor
import com.wynndie.spwallet.sharedCore.presentation.extensions.asDisplayableOre
import com.wynndie.spwallet.sharedCore.presentation.extensions.asFormattedAmount
import com.wynndie.spwallet.sharedCore.presentation.extensions.asPainter
import com.wynndie.spwallet.sharedCore.presentation.formatters.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.theme.AppTheme
import com.wynndie.spwallet.sharedCore.presentation.theme.sizes
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedFeature.home.presentation.component.ActionButtons
import com.wynndie.spwallet.sharedFeature.home.presentation.component.AuthCardOffer
import com.wynndie.spwallet.sharedFeature.home.presentation.component.AuthCardSheet
import com.wynndie.spwallet.sharedFeature.home.presentation.component.AuthedCardSheet
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.activate
import com.wynndie.spwallet.sharedResources.app_logo_foreground
import com.wynndie.spwallet.sharedResources.app_name
import com.wynndie.spwallet.sharedResources.auth_card_to_get_benefits
import com.wynndie.spwallet.sharedResources.bank_cards
import com.wynndie.spwallet.sharedResources.create
import com.wynndie.spwallet.sharedResources.custom_cards
import com.wynndie.spwallet.sharedResources.deactivate
import com.wynndie.spwallet.sharedResources.deactivate_card_description
import com.wynndie.spwallet.sharedResources.deactivate_card_title
import com.wynndie.spwallet.sharedResources.ic_add
import com.wynndie.spwallet.sharedResources.ic_add_card
import com.wynndie.spwallet.sharedResources.ic_reload
import com.wynndie.spwallet.sharedResources.no_authed_cards
import com.wynndie.spwallet.sharedResources.x_of_ore
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    if (state.isAuthCardSheetVisible) {
        AuthCardSheet(
            onDismiss = { viewModel.onAction(HomeAction.ToggleAuthCardSheet(false)) },
            loadingState = state.authLoadingState,
            isAuthButtonEnabled = state.isAuthButtonEnabled,
            cards = state.unauthedCards,
            initialPage = state.carouselPage,
            idInputState = state.idInputFieldState,
            onChangeIdValue = { viewModel.onAction(HomeAction.ChangeCardIdValue(it)) },
            onToggleCardIdFocus = { viewModel.onAction(HomeAction.ClearIdFocus) },
            tokenInputState = state.tokenInputFieldState,
            onChangeTokenValue = { viewModel.onAction(HomeAction.ChangeTokenValue(it)) },
            onToggleCardTokenFocus = { viewModel.onAction(HomeAction.ClearCardTokenFocus) },
            onClickAuthButton = { id, token -> viewModel.onAction(HomeAction.AuthCard(id, token)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = MaterialTheme.spacing.medium)
        )
    }

    if (state.isAuthedCardSheetVisible) {
        AuthedCardSheet(
            onDismiss = { viewModel.onAction(HomeAction.ToggleAuthedCardSheet(false)) },
            cards = state.authedCards,
            page = state.carouselPage,
            onDeleteButtonClick = { viewModel.onAction(HomeAction.ToggleDeleteCardDialog(true)) },
            onTransferBetweenCardsClick = { viewModel.onAction(HomeAction.TransferBetweenCards(it)) },
            onTransferButtonClick = { viewModel.onAction(HomeAction.TransferByCard(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = MaterialTheme.spacing.medium)
        )
    }

    if (state.isDeactivateCardDialogVisible) {
        Dialog(
            onConfirm = {
                val card = state.authedCards[state.carouselPage]
                viewModel.onAction(HomeAction.DeactivateCard(card))
            },
            onDismiss = { viewModel.onAction(HomeAction.ToggleDeleteCardDialog(false)) },
            title = stringResource(Res.string.deactivate_card_title),
            description = stringResource(Res.string.deactivate_card_description),
            confirmButtonText = stringResource(Res.string.deactivate),
            destructive = true
        )
    }


    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        topBar = {
            TopAppBar(
                title = state.authedUser.name.ifBlank {
                    stringResource(Res.string.app_name)
                },
                titleSlots = {
                    Image(
                        painter = painterResource(Res.drawable.app_logo_foreground),
                        contentDescription = null,
                        modifier = Modifier.size(MaterialTheme.sizes.small)
                    )
                },
                actions = {
                    if (state.authedUser.name.isNotBlank()) {
                        IconButton(
                            icon = painterResource(Res.drawable.ic_reload),
                            onClick = { viewModel.onAction(HomeAction.Refresh) },
                            color = MaterialTheme.colorScheme.onSurface,
                            loading = state.screenLoadingState == LoadingState.Loading
                        )
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

        Crossfade(state.screenLoadingState) { screenState ->

            when (screenState) {
                LoadingState.Loading -> {
                    LoadingScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }

                is LoadingState.Failed -> {

                }

                LoadingState.Finished -> {
                    HomeScreenContent(
                        state = state,
                        onAction = viewModel::onAction,
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeScreenContent(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val isUserAuthed = state.authedCards.isNotEmpty()
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraLarge),
        modifier = modifier
    ) {
        BalanceComponent(
            balance = state.totalBalance.asDisplayableOre(),
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
        )

        MultiChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.medium)
                .clip(MaterialTheme.shapes.medium)
        ) {
            SpServers.entries.forEach { server ->
                SegmentedButton(
                    label = server.label,
                    selected = server == state.selectedServer,
                    onClick = { onAction(HomeAction.SelectServer(server)) }
                )
            }
        }

        if (isUserAuthed) {
            ActionButtons(
                onAuthCardClick = { onAction(HomeAction.ToggleAuthCardSheet(true)) },
                onTransferBetweenCardsClick = { onAction(HomeAction.TransferBetweenCards("")) },
                onTransferByNumberClick = { onAction(HomeAction.TransferByCard("")) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium)
            )
        }

        TitledContent(
            title = stringResource(Res.string.bank_cards)
        ) {
            if (state.authedCards.isNotEmpty()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                ) {
                    state.authedCards.forEach { card ->
                        AccountCardTile(
                            label = "${card.number} • ${card.name}",
                            title = stringResource(Res.string.x_of_ore, card.balance)
                                .asFormattedAmount().uppercase(),
                            text = card.balance.asDisplayableOre().formatted,
                            icon = card.icon.asPainter(),
                            color = card.color.asColor(),
                            onClick = { onAction(HomeAction.SelectAuthedCard(card.id)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = MaterialTheme.spacing.medium)
                        )
                    }

                    TonalButton(
                        text = stringResource(Res.string.activate),
                        icon = painterResource(Res.drawable.ic_add_card),
                        onClick = { onAction(HomeAction.ToggleAuthCardSheet(true)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.spacing.medium)
                    )
                }
            } else {
                AuthCardOffer(
                    title = stringResource(Res.string.no_authed_cards),
                    description = stringResource(Res.string.auth_card_to_get_benefits),
                    onClickAuthCard = { onAction(HomeAction.ToggleAuthCardSheet(true)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.medium)
                )
            }
        }

        TitledContent(
            title = stringResource(Res.string.custom_cards)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                state.customCards.forEach { card ->
                    AccountCardTile(
                        label = card.name,
                        title = stringResource(Res.string.x_of_ore, card.balance)
                            .asFormattedAmount().uppercase(),
                        text = card.balance.asDisplayableOre().formatted,
                        icon = card.icon.asPainter(),
                        color = card.color.asColor(),
                        onClick = { onAction(HomeAction.SelectCustomCard(card.id)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.spacing.medium)
                    )
                }

                TonalButton(
                    text = stringResource(Res.string.create),
                    icon = painterResource(Res.drawable.ic_add),
                    onClick = { onAction(HomeAction.SelectCustomCard("")) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.medium)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenContentPreview() {
    AppTheme {
        HomeScreenContent(
            state = HomeState(
                authedCards = listOf(
                    createAuthedCard(
                        name = "asdf",
                        number = "3245",
                        balance = 1234
                    )
                )
            ),
            onAction = { },
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.medium)
        )
    }
}