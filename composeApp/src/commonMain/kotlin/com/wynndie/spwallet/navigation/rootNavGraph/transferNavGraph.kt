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
import com.wynndie.spwallet.navigation.TransferNavGraphRoutes
import com.wynndie.spwallet.navigation.koinViewModelWithArgs
import com.wynndie.spwallet.navigation.sharedKoinViewModel
import com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.RecipientViewModel
import com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.searchRecipient.SearchRecipientNavEvent
import com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.searchRecipient.SearchRecipientScreenRoot
import com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.searchRecipient.SearchRecipientViewModel
import com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.transferBetweenCards.TransferBetweenCardsArgs
import com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.transferBetweenCards.TransferBetweenCardsNavEvent
import com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.transferBetweenCards.TransferBetweenCardsScreenRoot
import com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.transferBetweenCards.TransferBetweenCardsViewModel
import com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.transferByCard.TransferByCardNavEvent
import com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.transferByCard.TransferByCardScreenRoot
import com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.transferByCard.TransferByCardViewModel
import com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.transferByCard.TransferByCardViewModelArgs
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.transferNavGraph(
    navController: NavController
) {

    navigation<TransferNavGraphRoutes.TransferNavGraph>(
        startDestination = TransferNavGraphRoutes.SearchRecipient()
    ) {

        composable<TransferNavGraphRoutes.SearchRecipient> { navBackStackEntry ->

            val args = navBackStackEntry.toRoute<TransferNavGraphRoutes.SearchRecipient>()

            val recipientViewModel =
                navBackStackEntry.sharedKoinViewModel<RecipientViewModel>(navController)

            val viewModel = koinViewModel<SearchRecipientViewModel>()

            ObserveNavEvent<SearchRecipientNavEvent> { navEvent ->
                when (navEvent) {
                    SearchRecipientNavEvent.OnClickBack -> {
                        navController.navigateUp()
                    }

                    is SearchRecipientNavEvent.OnClickRecipient -> {
                        recipientViewModel.setRecipientId(navEvent.id)
                        recipientViewModel.setRecipientCardNumber(navEvent.cardNumber)

                        navController.navigate(
                            TransferNavGraphRoutes.TransferByCardNumber(
                                cardId = args.cardId,
                            )
                        ) {
                            launchSingleTop = true
                        }
                    }

                    is SearchRecipientNavEvent.OnClickEditRecipient -> {

                    }
                }
            }

            SearchRecipientScreenRoot(
                viewModel = viewModel
            )
        }

        composable<TransferNavGraphRoutes.EditSearchRecipient> { navBackStackEntry ->

            val recipientViewModel =
                navBackStackEntry.sharedKoinViewModel<RecipientViewModel>(navController)

            val viewModel = koinViewModel<SearchRecipientViewModel>()

            ObserveNavEvent<SearchRecipientNavEvent> { navEvent ->
                when (navEvent) {
                    SearchRecipientNavEvent.OnClickBack -> {
                        navController.navigateUp()
                    }

                    is SearchRecipientNavEvent.OnClickRecipient -> {
                        recipientViewModel.setRecipientId(navEvent.id)
                        recipientViewModel.setRecipientCardNumber(navEvent.cardNumber)

                        navController.navigateUp()
                    }

                    is SearchRecipientNavEvent.OnClickEditRecipient -> {

                    }
                }
            }

            SearchRecipientScreenRoot(
                viewModel = viewModel
            )
        }

        composable<TransferNavGraphRoutes.TransferByCardNumber> { navBackStackEntry ->

            val args = navBackStackEntry.toRoute<TransferNavGraphRoutes.TransferByCardNumber>()

            val recipientViewModel =
                navBackStackEntry.sharedKoinViewModel<RecipientViewModel>(navController)

            val viewModel = koinViewModelWithArgs<TransferByCardViewModel>(
                TransferByCardViewModelArgs(
                    cardId = args.cardId
                )
            )

            ObserveNavEvent<TransferByCardNavEvent> { navEvent ->
                when (navEvent) {
                    TransferByCardNavEvent.OnClickBack -> {
                        navController.navigateUp()
                    }

                    TransferByCardNavEvent.OnClickRecipient -> {
                        navController.navigate(TransferNavGraphRoutes.EditSearchRecipient) {
                            launchSingleTop = true
                        }
                    }
                }
            }

            val recipientId by recipientViewModel.recipientId.collectAsStateWithLifecycle()
            val recipientCardNumber by recipientViewModel.recipientCardNumber.collectAsStateWithLifecycle()
            LaunchedEffect(recipientId, recipientCardNumber) {
                viewModel.updateRecipient(recipientId, recipientCardNumber)
            }

            TransferByCardScreenRoot(
                viewModel = viewModel
            )
        }

        composable<TransferNavGraphRoutes.TransferBetweenCards> { navBackStackEntry ->

            val args = navBackStackEntry.toRoute<TransferNavGraphRoutes.TransferBetweenCards>()

            val viewModel = koinViewModelWithArgs<TransferBetweenCardsViewModel>(
                TransferBetweenCardsArgs(args.cardId)
            )

            ObserveNavEvent<TransferBetweenCardsNavEvent> { navEvent ->
                when (navEvent) {
                    TransferBetweenCardsNavEvent.OnClickBack -> {
                        navController.navigateUp()
                    }
                }
            }

            TransferBetweenCardsScreenRoot(
                viewModel = viewModel
            )
        }
    }
}