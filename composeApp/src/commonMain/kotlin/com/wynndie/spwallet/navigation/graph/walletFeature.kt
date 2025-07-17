package com.wynndie.spwallet.navigation.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.wynndie.spwallet.navigation.WalletNavGraph
import com.wynndie.spwallet.sharedFeature.home.presentation.screen.cash_card.CashCardScreenRoot
import com.wynndie.spwallet.sharedFeature.home.presentation.screen.cash_card.CashCardViewModel
import com.wynndie.spwallet.sharedFeature.home.presentation.screen.cash_card.CashCardViewModelArgs
import com.wynndie.spwallet.sharedFeature.home.presentation.screen.home.HomeScreenRoot
import com.wynndie.spwallet.sharedFeature.home.presentation.screen.home.HomeViewModel
import com.wynndie.spwallet.sharedFeature.home.presentation.screen.transfer_by_card.TransferByCardScreenRoot
import com.wynndie.spwallet.sharedFeature.home.presentation.screen.transfer_by_card.TransferByCardViewModel
import com.wynndie.spwallet.sharedFeature.home.presentation.screen.transfer_by_card.TransferByCardViewModelArgs
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.walletFeature(navController: NavController) {
    navigation<WalletNavGraph.RootGraph>(
        startDestination = WalletNavGraph.Home
    ) {
        composable<WalletNavGraph.Home> {
            val viewModel = koinViewModel<HomeViewModel>()

            HomeScreenRoot(
                viewModel = viewModel,
                onClickCashCard = { cardId ->
                    navController.navigate(
                        route = WalletNavGraph.CashCard(
                            cardId = cardId
                        )
                    )
                },
                onClickTransferByCard = { cardId ->
                    navController.navigate(
                        route = WalletNavGraph.TransferByCard(cardId = cardId)
                    )
                }
            )
        }

        composable<WalletNavGraph.CashCard> { navBackStackEntry ->
            val args = navBackStackEntry.toRoute<WalletNavGraph.CashCard>()
            val viewModel = koinViewModel<CashCardViewModel> {
                parametersOf(
                    CashCardViewModelArgs(
                        cardId = args.cardId
                    )
                )
            }

            CashCardScreenRoot(
                viewModel = viewModel,
                navigateBack = { navController.navigateUp() }
            )
        }

        composable<WalletNavGraph.TransferByCard> { navBackStackEntry ->
            val args = navBackStackEntry.toRoute<WalletNavGraph.TransferByCard>()
            val viewModel = koinViewModel<TransferByCardViewModel> {
                parametersOf(
                    TransferByCardViewModelArgs(
                        cardId = args.cardId
                    )
                )
            }

            TransferByCardScreenRoot(
                viewModel = viewModel,
                navigateBack = { navController.navigateUp() }
            )
        }
    }
}