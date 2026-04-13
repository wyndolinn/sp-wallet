package com.wynndie.spwallet.navigation.rootNavGraph

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.wynndie.spwallet.navigation.ObserveNavEvent
import com.wynndie.spwallet.navigation.Route
import com.wynndie.spwallet.navigation.sharedKoinViewModel
import com.wynndie.spwallet.sharedFeature.transfer.presentation.RecipientViewModel
import com.wynndie.spwallet.sharedFeature.transfer.presentation.searchRecipient.SearchRecipientNavEvent
import com.wynndie.spwallet.sharedFeature.transfer.presentation.searchRecipient.SearchRecipientScreenRoot
import com.wynndie.spwallet.sharedFeature.transfer.presentation.searchRecipient.SearchRecipientViewModel
import com.wynndie.spwallet.sharedFeature.transfer.presentation.transferBetweenCards.TransferBetweenCardsNavEvent
import com.wynndie.spwallet.sharedFeature.transfer.presentation.transferBetweenCards.TransferBetweenCardsParams
import com.wynndie.spwallet.sharedFeature.transfer.presentation.transferBetweenCards.TransferBetweenCardsScreenRoot
import com.wynndie.spwallet.sharedFeature.transfer.presentation.transferBetweenCards.TransferBetweenCardsViewModel
import com.wynndie.spwallet.sharedFeature.transfer.presentation.transferByCard.TransferByCardNavEvent
import com.wynndie.spwallet.sharedFeature.transfer.presentation.transferByCard.TransferByCardParams
import com.wynndie.spwallet.sharedFeature.transfer.presentation.transferByCard.TransferByCardScreenRoot
import com.wynndie.spwallet.sharedFeature.transfer.presentation.transferByCard.TransferByCardViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.transferNavGraph(
    navController: NavController
) {

    navigation<Route.TransferNavGraph>(
        startDestination = Route.TransferNavGraph.SearchRecipient()
    ) {

        composable<Route.TransferNavGraph.SearchRecipient> { navBackStackEntry ->

            val args = navBackStackEntry.toRoute<Route.TransferNavGraph.SearchRecipient>()

            val recipientViewModel =
                navBackStackEntry.sharedKoinViewModel<RecipientViewModel>(navController)

            val viewModel = koinViewModel<SearchRecipientViewModel>()

            ObserveNavEvent<SearchRecipientNavEvent> { navEvent ->
                when (navEvent) {
                    SearchRecipientNavEvent.OnClickBack -> {
                        navController.navigateUp()
                    }

                    is SearchRecipientNavEvent.OnClickRecipient -> {
                        recipientViewModel.setRecipientCardNumber(navEvent.cardNumber)

                        navController.navigate(
                            Route.TransferNavGraph.TransferByCardNumber(
                                cardId = args.cardId,
                            )
                        ) {
                            launchSingleTop = true
                        }
                    }

                    is SearchRecipientNavEvent.OnClickAddRecipient -> {

                    }
                }
            }

            SearchRecipientScreenRoot(
                viewModel = viewModel
            )
        }

        composable<Route.TransferNavGraph.EditRecipient> { navBackStackEntry ->

            val recipientViewModel =
                navBackStackEntry.sharedKoinViewModel<RecipientViewModel>(navController)

            val viewModel = koinViewModel<SearchRecipientViewModel>()

            ObserveNavEvent<SearchRecipientNavEvent> { navEvent ->
                when (navEvent) {
                    SearchRecipientNavEvent.OnClickBack -> {
                        navController.navigateUp()
                    }

                    is SearchRecipientNavEvent.OnClickRecipient -> {
                        recipientViewModel.setRecipientCardNumber(navEvent.cardNumber)

                        navController.navigateUp()
                    }

                    is SearchRecipientNavEvent.OnClickAddRecipient -> {

                    }
                }
            }

            SearchRecipientScreenRoot(
                viewModel = viewModel
            )
        }

        composable<Route.TransferNavGraph.TransferByCardNumber> { navBackStackEntry ->

            val args = navBackStackEntry.toRoute<Route.TransferNavGraph.TransferByCardNumber>()

            val recipientViewModel =
                navBackStackEntry.sharedKoinViewModel<RecipientViewModel>(navController)

            val viewModel = koinViewModel<TransferByCardViewModel> {
                parametersOf(TransferByCardParams(args.cardId))
            }

            ObserveNavEvent<TransferByCardNavEvent> { navEvent ->
                when (navEvent) {
                    TransferByCardNavEvent.OnClickBack -> {
                        navController.navigateUp()
                    }

                    TransferByCardNavEvent.OnClickRecipient -> {
                        navController.navigate(Route.TransferNavGraph.EditRecipient) {
                            launchSingleTop = true
                        }
                    }

                    TransferByCardNavEvent.OnTransferSuccess -> {
                        navController.popBackStack(
                            route = Route.TransferNavGraph,
                            inclusive = true
                        )
                    }
                }
            }

            val recipientCardNumber by recipientViewModel.recipientCardNumber.collectAsStateWithLifecycle()
            LaunchedEffect(recipientCardNumber) {
                viewModel.updateRecipient(recipientCardNumber)
            }

            TransferByCardScreenRoot(
                viewModel = viewModel
            )
        }

        composable<Route.TransferNavGraph.TransferBetweenCards> { navBackStackEntry ->

            val args = navBackStackEntry.toRoute<Route.TransferNavGraph.TransferBetweenCards>()

            val viewModel = koinViewModel<TransferBetweenCardsViewModel> {
                parametersOf(TransferBetweenCardsParams(args.cardId))
            }

            ObserveNavEvent<TransferBetweenCardsNavEvent> { navEvent ->
                when (navEvent) {
                    TransferBetweenCardsNavEvent.OnClickBack -> {
                        navController.navigateUp()
                    }

                    TransferBetweenCardsNavEvent.OnTransferSuccess -> {
                        navController.popBackStack(
                            route = Route.TransferNavGraph,
                            inclusive = true
                        )
                    }
                }
            }

            TransferBetweenCardsScreenRoot(
                viewModel = viewModel
            )
        }
    }
}