package com.wynndie.spwallet.navigation.rootNavGraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.wynndie.spwallet.navigation.HomeNavGraphRoutes
import com.wynndie.spwallet.navigation.ProfileNavGraphRoutes
import com.wynndie.spwallet.navigation.TransferNavGraphRoutes
import com.wynndie.spwallet.sharedFeature.home.presentation.screen.cash_card.CashCardScreenRoot
import com.wynndie.spwallet.sharedFeature.home.presentation.screen.cash_card.CashCardViewModel
import com.wynndie.spwallet.sharedFeature.home.presentation.screen.cash_card.CashCardViewModelArgs
import com.wynndie.spwallet.sharedFeature.home.presentation.screen.home.HomeScreenRoot
import com.wynndie.spwallet.sharedFeature.home.presentation.screen.home.HomeViewModel
import com.wynndie.spwallet.sharedFeature.home.presentation.screen.home.HomeViewModelArgs
import com.wynndie.spwallet.sharedFeature.home.presentation.screen.transfer_by_card.TransferByCardScreenRoot
import com.wynndie.spwallet.sharedFeature.home.presentation.screen.transfer_by_card.TransferByCardViewModel
import com.wynndie.spwallet.sharedFeature.home.presentation.screen.transfer_by_card.TransferByCardViewModelArgs
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.homeNavGraph(
    navController: NavController
) {

    navigation<HomeNavGraphRoutes.HomeNavGraph>(
        startDestination = HomeNavGraphRoutes.Home
    ) {

        composable<HomeNavGraphRoutes.Home> {
            val viewModel = koinViewModel<HomeViewModel> {
                parametersOf(
                    HomeViewModelArgs(
                        onClickTopAppBar = {
                            navController.navigate(ProfileNavGraphRoutes.ProfileNavGraph) {
                                launchSingleTop = true
                            }
                        },
                        onClickCashCard = { cardId ->
                            navController.navigate(HomeNavGraphRoutes.CashCard(cardId)) {
                                launchSingleTop = true
                            }
                        },
                        onClickTransferByCard = { cardId ->
                            navController.navigate(TransferNavGraphRoutes.SearchRecipient(cardId)) {
                                launchSingleTop = true
                            }
                        }
                    )
                )
            }

            HomeScreenRoot(
                viewModel = viewModel
            )
        }

        composable<HomeNavGraphRoutes.CashCard> { navBackStackEntry ->
            val args = navBackStackEntry.toRoute<HomeNavGraphRoutes.CashCard>()
            val viewModel = koinViewModel<CashCardViewModel> {
                parametersOf(
                    CashCardViewModelArgs(
                        cardId = args.cardId,
                        onClickBack = { navController.navigateUp() }
                    )
                )
            }

            CashCardScreenRoot(
                viewModel = viewModel
            )
        }

//        composable<HomeNavGraphRoutes.TransferByCard> { navBackStackEntry ->
//            val args = navBackStackEntry.toRoute<HomeNavGraphRoutes.TransferByCard>()
//            val viewModel = koinViewModel<TransferByCardViewModel> {
//                parametersOf(
//                    TransferByCardViewModelArgs(
//                        cardId = args.cardId,
//                        onClickBack = { navController.navigateUp() }
//                    )
//                )
//            }
//
//            TransferByCardScreenRoot(
//                viewModel = viewModel
//            )
//        }
    }
}