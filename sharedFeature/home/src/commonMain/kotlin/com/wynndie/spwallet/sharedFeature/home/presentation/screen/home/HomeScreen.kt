package com.wynndie.spwallet.sharedFeature.home.presentation.screen.home

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.wynndie.spwallet.sharedCore.domain.constants.Constants
import com.wynndie.spwallet.sharedCore.presentation.component.appDesignSystem.AppCardTileList
import com.wynndie.spwallet.sharedCore.presentation.component.loading.LoadingScreen
import com.wynndie.spwallet.sharedCore.presentation.mapper.joinToUiText
import com.wynndie.spwallet.sharedCore.presentation.model.LoadingState
import com.wynndie.spwallet.sharedFeature.home.presentation.screen.home.component.ActionButtons
import com.wynndie.spwallet.sharedFeature.home.presentation.screen.home.component.AppBarContent
import com.wynndie.spwallet.sharedFeature.home.presentation.screen.home.component.AuthCardOffer
import com.wynndie.spwallet.sharedFeature.home.presentation.screen.home.component.AuthCardSheet
import com.wynndie.spwallet.sharedFeature.home.presentation.screen.home.component.AuthedCardSheet
import com.wynndie.spwallet.sharedFeature.home.presentation.screen.home.component.DeactivateCardDialog
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.activate_cards
import com.wynndie.spwallet.sharedResources.app_logo_foreground
import com.wynndie.spwallet.sharedResources.app_name
import com.wynndie.spwallet.sharedResources.bank_cards
import com.wynndie.spwallet.sharedResources.cash_cards
import com.wynndie.spwallet.sharedResources.create
import com.wynndie.spwallet.sharedResources.total_balance
import com.wynndie.spwallet.sharedResources.total_of_ore
import com.wynndie.spwallet.sharedtheme.designSystem.button.BaseIconButton
import com.wynndie.spwallet.sharedtheme.designSystem.button.BaseOutlinedButton
import com.wynndie.spwallet.sharedtheme.designSystem.infoPanel.BaseInfoPanelMedium
import com.wynndie.spwallet.sharedtheme.theme.AppTheme
import com.wynndie.spwallet.sharedtheme.theme.spacing
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    if (state.isAuthCardSheetVisible) {
        AuthCardSheet(
            onDismiss = { viewModel.onAction(HomeAction.OnToggleAuthCardSheet) },
            loadingState = state.authLoadingState,
            isAuthButtonEnabled = state.authLoadingState !is LoadingState.Loading,
            cards = state.unauthedCards.map { it.asTile() },
            initialPage = state.carouselPage,
            idInputFieldState = state.idInputFieldState,
            onChangeIdValue = { viewModel.onAction(HomeAction.OnChangeCardIdValue(it)) },
            tokenInputFieldState = state.tokenInputFieldState,
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
            cards = state.authedCards.map { it.asTile() },
            page = state.carouselPage,
            onDeleteButtonClick = { viewModel.onAction(HomeAction.OnToggleDeleteCardDialog) },
            onTransferButtonClick = { viewModel.onAction(HomeAction.OnClickTransferByCard(it)) },
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
                        BaseIconButton(
                            icon = Icons.Outlined.Refresh,
                            onClick = { viewModel.onAction(HomeAction.OnRefresh) },
                            loading = state.screenLoadingState == LoadingState.Loading
                        )
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

            BaseInfoPanelMedium(
                label = stringResource(Res.string.total_balance),
                title = stringResource(Res.string.total_of_ore, state.totalBalance.value),
                description = state.totalBalance.formatted.joinToUiText(" ").asString()
            )
        }

        item {
            if (isUserAuthed) {
                ActionButtons(
                    onAuthCardClick = { onAction(HomeAction.OnToggleAuthCardSheet) },
                    onTransferByNumberClick = { onAction(HomeAction.OnClickTransferByCard(null)) },
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
                    AppCardTileList(
                        title = stringResource(Res.string.bank_cards),
                        tiles = state.authedCards.map { it.asTile() },
                        onClickTile = { onAction(HomeAction.OnClickAuthedCard(it.id)) }
                    )
                }

                AppCardTileList(
                    title = stringResource(Res.string.cash_cards, state.cashCards.size, 5),
                    tiles = state.cashCards.map { it.asTile() },
                    onClickTile = { onAction(HomeAction.OnClickCashCard(it.id)) },
                    trailingContent = if (state.cashCards.size < Constants.MAX_CASH_CARDS_AMOUNT) {
                        {
                            BaseOutlinedButton(
                                text = stringResource(Res.string.create),
                                onClick = { onAction(HomeAction.OnClickCashCard(null)) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    } else null
                )

                if (state.unauthedCards.isNotEmpty()) {
                    AppCardTileList(
                        title = stringResource(Res.string.activate_cards),
                        tiles = state.unauthedCards.map { it.asTile() },
                        onClickTile = { onAction(HomeAction.OnClickUnauthedCard(it.id)) }
                    )
                }
            }

            Spacer(Modifier.height(MaterialTheme.spacing.medium))
        }
    }
}

@Preview
@Composable
fun HomeScreenContentPreview() {
    AppTheme {
        HomeScreenContent(
            state = HomeState(),
            onAction = { },
            contentPadding = PaddingValues(MaterialTheme.spacing.medium),
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        )
    }
}