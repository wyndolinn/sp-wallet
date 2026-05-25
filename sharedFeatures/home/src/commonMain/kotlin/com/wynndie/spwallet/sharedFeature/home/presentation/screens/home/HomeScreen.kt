package com.wynndie.spwallet.sharedFeature.home.presentation.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.activate
import com.wynndie.spwallet.sharedCore.app_name
import com.wynndie.spwallet.sharedCore.auth_card
import com.wynndie.spwallet.sharedCore.auth_card_info
import com.wynndie.spwallet.sharedCore.bank_cards
import com.wynndie.spwallet.sharedCore.create
import com.wynndie.spwallet.sharedCore.custom_cards
import com.wynndie.spwallet.sharedCore.deactivate
import com.wynndie.spwallet.sharedCore.deactivate_card_description
import com.wynndie.spwallet.sharedCore.deactivate_card_title
import com.wynndie.spwallet.sharedCore.domain.constructors.createAuthedCard
import com.wynndie.spwallet.sharedCore.domain.models.SpServers
import com.wynndie.spwallet.sharedCore.ic_arrow_front
import com.wynndie.spwallet.sharedCore.img_logo
import com.wynndie.spwallet.sharedCore.presentation.components.AsyncImage
import com.wynndie.spwallet.sharedCore.presentation.components.InformationCard
import com.wynndie.spwallet.sharedCore.presentation.components.TitledContent
import com.wynndie.spwallet.sharedCore.presentation.components.TopAppBar
import com.wynndie.spwallet.sharedCore.presentation.components.buttons.OutlinedButton
import com.wynndie.spwallet.sharedCore.presentation.components.buttons.SegmentedButton
import com.wynndie.spwallet.sharedCore.presentation.components.overlays.Dialog
import com.wynndie.spwallet.sharedCore.presentation.components.screen.Scaffold
import com.wynndie.spwallet.sharedCore.presentation.components.screen.ScreenLayout
import com.wynndie.spwallet.sharedCore.presentation.components.tiles.AccountCardTile
import com.wynndie.spwallet.sharedCore.presentation.extensions.add
import com.wynndie.spwallet.sharedCore.presentation.extensions.asColor
import com.wynndie.spwallet.sharedCore.presentation.extensions.asDisplayableOre
import com.wynndie.spwallet.sharedCore.presentation.extensions.asFormattedAmount
import com.wynndie.spwallet.sharedCore.presentation.extensions.asPainter
import com.wynndie.spwallet.sharedCore.presentation.extensions.remove
import com.wynndie.spwallet.sharedCore.presentation.extensions.thenIfElse
import com.wynndie.spwallet.sharedCore.presentation.formatters.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.theme.AppTheme
import com.wynndie.spwallet.sharedCore.presentation.theme.sizes
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedCore.transfer
import com.wynndie.spwallet.sharedCore.x_of_ore
import com.wynndie.spwallet.sharedFeature.home.presentation.screens.home.component.ActionButtons
import com.wynndie.spwallet.sharedFeature.home.presentation.screens.home.component.AuthedCardSheet
import com.wynndie.spwallet.sharedFeature.home.presentation.screens.home.component.BalanceComponent
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()
    var lastScrollValue by remember { mutableIntStateOf(0) }
    val isExpanded by remember {
        derivedStateOf {
            val expanded = scrollState.value <= lastScrollValue
            lastScrollValue = scrollState.value
            expanded
        }
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


    val username = state.authedUser.name
    Scaffold(
        topBar = {
            TopAppBar(
                title = username.ifBlank {
                    stringResource(Res.string.app_name)
                },
                titleSlots = {
                    if (username.isNotBlank()) {
                        AsyncImage(
                            url = "https://avatars.spworlds.ru/face/$username?w=32",
                            contentDescription = null,
                            error = painterResource(Res.drawable.img_logo),
                            modifier = Modifier.size(MaterialTheme.sizes.small)
                        )
                    } else {
                        Image(
                            painter = painterResource(Res.drawable.img_logo),
                            contentDescription = null,
                            modifier = Modifier.size(MaterialTheme.sizes.small)
                        )
                    }
                },
                actions = {
                    MultiChoiceSegmentedButtonRow(
                        space = 0.dp,
                        modifier = Modifier
                            .padding(horizontal = MaterialTheme.spacing.medium)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                            .thenIfElse(
                                condition = state.screenLoadingState == LoadingState.Loading,
                                onTrue = {
                                    Modifier.border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = CircleShape
                                    )
                                },
                                onFalse = {
                                    Modifier.border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = CircleShape
                                    )
                                }
                            )
                            .height(MaterialTheme.sizes.small)
                    ) {
                        SpServers.entries.forEach { server ->
                            SegmentedButton(
                                label = server.label,
                                selected = server == state.selectedServer,
                                onClick = { viewModel.onAction(HomeAction.SelectServer(server)) },
                                enabled = state.screenLoadingState != LoadingState.Loading,
                                modifier = Modifier.fillMaxHeight()
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            val hasCards by derivedStateOf { state.authedCards.isNotEmpty() }
            val finishedLoading by derivedStateOf { state.screenLoadingState == LoadingState.Finished }
            AnimatedVisibility(
                visible = hasCards && finishedLoading,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
            ) {
                ExtendedFloatingActionButton(
                    onClick = { viewModel.onAction(HomeAction.TransferByCard("")) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        focusedElevation = 0.dp,
                        hoveredElevation = 0.dp
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AnimatedVisibility(
                            visible = isExpanded,
                            enter = fadeIn() + expandHorizontally(),
                            exit = fadeOut() + shrinkHorizontally()
                        ) {
                            Text(
                                text = stringResource(Res.string.transfer),
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.padding(end = MaterialTheme.spacing.medium)
                            )
                        }

                        Icon(
                            painter = painterResource(Res.drawable.ic_arrow_front),
                            contentDescription = null
                        )
                    }
                }
            }
        },
        loadingState = state.screenLoadingState,
        modifier = modifier
    ) { innerPadding ->
        val layoutDirection = LocalLayoutDirection.current
        PullToRefreshBox(
            isRefreshing = state.screenLoadingState is LoadingState.Loading,
            onRefresh = { viewModel.onAction(HomeAction.Refresh) },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(layoutDirection),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(layoutDirection)
                )
        ) {
            ScreenLayout(
                contentPadding = innerPadding
                    .remove(top = innerPadding.calculateTopPadding())
                    .add(MaterialTheme.spacing.medium),
                modifier = Modifier.verticalScroll(scrollState)
            ) {
                Crossfade(state.selectedServer) {
                    HomeScreenContent(
                        state = state,
                        onAction = viewModel::onAction,
                        modifier = Modifier.fillMaxSize()
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
            hasCards = state.authedCards.isNotEmpty() || state.customCards.isNotEmpty(),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium)
        )

        if (isUserAuthed) {
            ActionButtons(
                onRecipientsClick = { onAction(HomeAction.EditRecipients) },
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
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
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

                    OutlinedButton(
                        text = stringResource(Res.string.activate),
                        onClick = { onAction(HomeAction.AuthCard) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.spacing.medium)
                    )
                }
            } else {
                InformationCard(
                    title = stringResource(Res.string.auth_card),
                    content = {
                        Text(
                            text = stringResource(Res.string.auth_card_info)
                        )
                    },
                    actions = {
                        OutlinedButton(
                            text = stringResource(Res.string.activate),
                            onClick = { onAction(HomeAction.AuthCard) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    shape = MaterialTheme.shapes.extraLarge,
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
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
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

                OutlinedButton(
                    text = stringResource(Res.string.create),
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