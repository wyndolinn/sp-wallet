package com.wynndie.spwallet.sharedFeature.home.presentation.screens.home

import androidx.compose.animation.AnimatedVisibility
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
import com.wynndie.spwallet.sharedCore.domain.constants.CoreConstants
import com.wynndie.spwallet.sharedCore.presentation.components.TitledContent
import com.wynndie.spwallet.sharedCore.presentation.components.loading.LoadingScreen
import com.wynndie.spwallet.sharedCore.presentation.components.tiles.cards.AuthedCardTile
import com.wynndie.spwallet.sharedCore.presentation.components.tiles.cards.CustomCardTile
import com.wynndie.spwallet.sharedCore.presentation.components.tiles.cards.UnauthedCardTile
import com.wynndie.spwallet.sharedCore.presentation.extensions.asColor
import com.wynndie.spwallet.sharedCore.presentation.extensions.asImage
import com.wynndie.spwallet.sharedCore.presentation.extensions.joinToUiText
import com.wynndie.spwallet.sharedCore.presentation.states.LoadingState
import com.wynndie.spwallet.sharedFeature.home.presentation.screens.home.component.ActionButtons
import com.wynndie.spwallet.sharedFeature.home.presentation.screens.home.component.AppBarContent
import com.wynndie.spwallet.sharedFeature.home.presentation.screens.home.component.AuthCardOffer
import com.wynndie.spwallet.sharedFeature.home.presentation.screens.home.component.AuthCardSheet
import com.wynndie.spwallet.sharedFeature.home.presentation.screens.home.component.AuthedCardSheet
import com.wynndie.spwallet.sharedFeature.home.presentation.screens.home.component.DeactivateCardDialog
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.activate_cards
import com.wynndie.spwallet.sharedResources.app_logo_foreground
import com.wynndie.spwallet.sharedResources.app_name
import com.wynndie.spwallet.sharedResources.bank_cards
import com.wynndie.spwallet.sharedResources.create
import com.wynndie.spwallet.sharedResources.custom_cards
import com.wynndie.spwallet.sharedResources.total_balance
import com.wynndie.spwallet.sharedResources.x_of_ore
import com.wynndie.spwallet.sharedtheme.designSystem.buttons.BaseIconButton
import com.wynndie.spwallet.sharedtheme.designSystem.buttons.BaseOutlinedButton
import com.wynndie.spwallet.sharedtheme.designSystem.infoLayouts.vertical.BaseInfoPanelMedium
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
            onDismiss = { viewModel.onAction(HomeAction.OnToggleAuthCardSheet(false)) },
            loadingState = state.authLoadingState,
            isAuthButtonEnabled = state.authLoadingState !is LoadingState.Loading,
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
            onDismiss = { viewModel.onAction(HomeAction.OnToggleAuthedCardSheet(false)) },
            cards = state.authedCards,
            page = state.carouselPage,
            onDeleteButtonClick = { viewModel.onAction(HomeAction.OnToggleDeleteCardDialog(false)) },
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
            onDismiss = { viewModel.onAction(HomeAction.OnToggleDeleteCardDialog(false)) },
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
                title = stringResource(Res.string.x_of_ore, state.totalBalance.value).uppercase(),
                description = if (state.totalBalance.value != 0L) {
                    state.totalBalance.formatted.joinToUiText(" ").asString()
                } else null
            )
        }

        item {
            if (isUserAuthed) {
                ActionButtons(
                    onAuthCardClick = { onAction(HomeAction.OnToggleAuthCardSheet(true)) },
                    onTransferBetweenCardsClick = {  },
                    onTransferByNumberClick = { onAction(HomeAction.OnClickTransferByCard(null)) },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                AuthCardOffer(
                    onClickAuthCard = {
                        onAction(HomeAction.OnToggleAuthCardSheet(true))
                    }
                )
            }
        }

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
            ) {

                if (state.authedCards.isNotEmpty()) {
                    TitledContent(
                        title = stringResource(Res.string.bank_cards)
                    ) {
                        state.authedCards.forEach { card ->
                            AuthedCardTile(
                                icon = card.icon.asImage(),
                                iconBackground = card.color.asColor(),
                                cardName = card.name,
                                cardNumber = card.number,
                                balance = card.balance,
                                onClick = { onAction(HomeAction.OnClickAuthedCard(card.id)) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                TitledContent(
                    title = stringResource(
                        Res.string.custom_cards,
                        state.customCards.size,
                        CoreConstants.MAX_CUSTOM_CARDS_AMOUNT
                    )
                ) {
                    state.customCards.forEach { card ->
                        CustomCardTile(
                            icon = card.icon.asImage(),
                            iconBackground = card.color.asColor(),
                            cardName = card.name,
                            balance = card.balance,
                            onClick = { onAction(HomeAction.OnClickCustomCard(card.id)) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    AnimatedVisibility(
                        visible = state.customCards.size < CoreConstants.MAX_CUSTOM_CARDS_AMOUNT
                    ) {
                        BaseOutlinedButton(
                            text = stringResource(Res.string.create),
                            onClick = { onAction(HomeAction.OnClickCustomCard(null)) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                if (state.unauthedCards.isNotEmpty()) {
                    TitledContent(
                        title = stringResource(Res.string.activate_cards)
                    ) {
                        state.unauthedCards.forEach { card ->
                            UnauthedCardTile(
                                icon = card.icon.asImage(),
                                iconBackground = card.color.asColor(),
                                cardName = card.name,
                                cardNumber = card.number,
                                onClick = { onAction(HomeAction.OnClickUnauthedCard(card.id)) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
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