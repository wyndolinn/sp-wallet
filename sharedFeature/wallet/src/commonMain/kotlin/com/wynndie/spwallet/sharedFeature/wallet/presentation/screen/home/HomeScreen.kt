package com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.home

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wynndie.spwallet.sharedCore.presentation.component.button.UiOutlinedButton
import com.wynndie.spwallet.sharedCore.presentation.component.infoDisplay.LargeInfoDisplay
import com.wynndie.spwallet.sharedCore.presentation.component.loading.LoadingScreen
import com.wynndie.spwallet.sharedCore.presentation.model.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.text.joinAsString
import com.wynndie.spwallet.sharedCore.presentation.theme.spacing
import com.wynndie.spwallet.sharedFeature.wallet.presentation.component.UiCardList
import com.wynndie.spwallet.sharedFeature.wallet.presentation.model.emptyCashCard
import com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.home.component.ActionButtons
import com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.home.component.AppBarContent
import com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.home.component.AuthCardOffer
import com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.home.component.dialog.AuthCardSheet
import com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.home.component.dialog.AuthedCardSheet
import com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.home.component.dialog.DeactivateCardDialog
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.activate_cards
import com.wynndie.spwallet.sharedResources.app_logo_foreground
import com.wynndie.spwallet.sharedResources.app_name
import com.wynndie.spwallet.sharedResources.bank_cards
import com.wynndie.spwallet.sharedResources.cash_cards
import com.wynndie.spwallet.sharedResources.create_cash_card
import com.wynndie.spwallet.sharedResources.total_balance
import com.wynndie.spwallet.sharedResources.total_of_ore
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel,
    onClickCashCard: (String) -> Unit,
    onClickTransferByCard: (String) -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    if (state.isAuthCardSheetVisible) {
        AuthCardSheet(
            onDismiss = { viewModel.onAction(HomeAction.OnToggleAuthCardSheet) },
            cards = state.unauthedCards,
            initialPage = state.carouselPage,
            idInputField = state.idInputField,
            onChangeIdValue = { viewModel.onAction(HomeAction.OnChangeCardIdValue(it)) },
            tokenInputField = state.tokenInputField,
            onChangeTokenValue = { viewModel.onAction(HomeAction.OnChangeCardTokenValue(it)) },
            onClickAuthButton = { id, token ->
                viewModel.onAction(
                    HomeAction.OnClickAuthCard(
                        id = id,
                        token = token
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = MaterialTheme.spacing.medium)
        )
    }

    if (state.isAuthedCardSheetVisible) {
        AuthedCardSheet(
            onDismiss = { viewModel.onAction(HomeAction.OnToggleAuthedCardSheet) },
            cards = state.authedCards.map {
                it.formatDescription()
            },
            page = state.carouselPage,
            onDeleteButtonClick = { viewModel.onAction(HomeAction.OnToggleDeleteCardDialog) },
            onTransferButtonClick = { card ->
                viewModel.onAction(
                    HomeAction.OnClickTransferByCard(
                        card = card,
                        navigate = onClickTransferByCard
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = MaterialTheme.spacing.medium)
        )
    }

    if (state.isDeactivateCardDialogVisible) {
        DeactivateCardDialog(
            onConfirm = {
                viewModel.onAction(
                    HomeAction.OnClickDeactivateCard(
                        card = state.authedCards[state.carouselPage]
                    )
                )
            },
            onDismiss = { viewModel.onAction(HomeAction.OnToggleDeleteCardDialog) },
            modifier = Modifier
        )
    }


    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        topBar = {
            if (state.authedCards.isNotEmpty()) {
                TopAppBar(
                    title = {
                        AppBarContent(
                            image = painterResource(Res.drawable.app_logo_foreground),
                            title = state.authedUser.name
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = { viewModel.onAction(HomeAction.OnRefresh) }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Refresh,
                                contentDescription = null
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            } else {
                TopAppBar(
                    title = {
                        AppBarContent(
                            image = painterResource(Res.drawable.app_logo_foreground),
                            title = stringResource(Res.string.app_name)
                        )
                    },
                    scrollBehavior = scrollBehavior
                )
            }
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
            animationSpec = tween(500)
        ) { screenState ->

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
                        onClickCashCard = onClickCashCard,
                        onClickTransferByCard = onClickTransferByCard,
                        contentPadding = innerPadding,
                        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
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
    onClickCashCard: (String) -> Unit,
    onClickTransferByCard: (String) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {

    val isUserAuthed = state.authedCards.isNotEmpty()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
        contentPadding = contentPadding,
        modifier = modifier
    ) {

        item {
            Spacer(Modifier.height(MaterialTheme.spacing.medium))

            LargeInfoDisplay(
                label = stringResource(Res.string.total_balance),
                title = stringResource(Res.string.total_of_ore, state.totalBalance.value),
                description = state.totalBalance.formatted.joinAsString(" ")
            )
        }

        item {
            if (isUserAuthed) {
                ActionButtons(
                    onAuthCardClick = { onAction(HomeAction.OnToggleAuthCardSheet) },
                    onTransferByNumberClick = {
                        onAction(
                            HomeAction.OnClickTransferByCard(
                                card = state.authedCards.first(),
                                navigate = onClickTransferByCard
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                AuthCardOffer(
                    onClickAuthCard = {
                        onAction(HomeAction.OnToggleAuthCardSheet)
                    }
                )
            }
        }

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
            ) {

                if (state.authedCards.isNotEmpty()) {
                    UiCardList(
                        listTitle = stringResource(Res.string.bank_cards),
                        cards = state.authedCards.map {
                            it.formatDescription()
                        },
                        onClick = {
                            onAction(HomeAction.OnClickAuthedCard(it))
                        }
                    )
                }

                UiCardList(
                    listTitle = stringResource(Res.string.cash_cards, state.cashCards.size, 5),
                    cards = state.cashCards.map {
                        it.formatDescription()
                    },
                    onClick = {
                        onAction(
                            HomeAction.OnClickCashCard(
                                card = it,
                                navigate = onClickCashCard
                            )
                        )
                    },
                    trailingContent = {
                        UiOutlinedButton(
                            text = stringResource(Res.string.create_cash_card),
                            onClick = {
                                onAction(
                                    HomeAction.OnClickCashCard(
                                        card = emptyCashCard,
                                        navigate = onClickCashCard
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                )

                if (state.unauthedCards.isNotEmpty()) {
                    UiCardList(
                        listTitle = stringResource(Res.string.activate_cards),
                        cards = state.unauthedCards,
                        onClick = {
                            onAction(HomeAction.OnClickUnauthedCard(it))
                        }
                    )
                }
            }

            Spacer(Modifier.height(MaterialTheme.spacing.medium))
        }
    }
}