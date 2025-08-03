package com.wynndie.spwallet.navigation.rootNavGraph

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.wynndie.spwallet.navigation.TransferNavGraphRoutes
import com.wynndie.spwallet.navigation.sharedKoinViewModel
import com.wynndie.spwallet.sharedFeature.transfer.presentation.screen.RecipientViewModel
import com.wynndie.spwallet.sharedFeature.transfer.presentation.screen.searchRecipient.SearchRecipientScreenRoot
import com.wynndie.spwallet.sharedFeature.transfer.presentation.screen.searchRecipient.SearchRecipientViewModel
import com.wynndie.spwallet.sharedFeature.transfer.presentation.screen.searchRecipient.SearchRecipientViewModelArgs
import com.wynndie.spwallet.sharedFeature.transfer.presentation.screen.transferByCard.TransferByCardScreenRoot
import com.wynndie.spwallet.sharedFeature.transfer.presentation.screen.transferByCard.TransferByCardViewModel
import com.wynndie.spwallet.sharedFeature.transfer.presentation.screen.transferByCard.TransferByCardViewModelArgs
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

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


            val viewModel = koinViewModel<SearchRecipientViewModel> {
                parametersOf(
                    SearchRecipientViewModelArgs(
                        onClickBack = { navController.navigateUp() },
                        onClickRecipient = { recipientId, cardNumber ->
                            recipientViewModel.setRecipientId(recipientId)
                            recipientViewModel.setRecipientCardNumber(cardNumber)

                            navController.navigate(
                                TransferNavGraphRoutes.TransferByCardNumber(
                                    cardId = args.cardId,
                                )
                            ) {
                                launchSingleTop = true
                            }
                        },
                        onClickEditRecipient = {

                        }
                    )
                )
            }

            SearchRecipientScreenRoot(
                viewModel = viewModel
            )
        }

        composable<TransferNavGraphRoutes.EditSearchRecipient> { navBackStackEntry ->

            val args = navBackStackEntry.toRoute<TransferNavGraphRoutes.SearchRecipient>()

            val recipientViewModel =
                navBackStackEntry.sharedKoinViewModel<RecipientViewModel>(navController)

            val viewModel = koinViewModel<SearchRecipientViewModel> {
                parametersOf(
                    SearchRecipientViewModelArgs(
                        onClickBack = { navController.navigateUp() },
                        onClickRecipient = { recipientId, cardNumber ->
                            recipientViewModel.setRecipientId(recipientId)
                            recipientViewModel.setRecipientCardNumber(cardNumber)

                            navController.navigateUp()
                        },
                        onClickEditRecipient = {

                        }
                    )
                )
            }

            SearchRecipientScreenRoot(
                viewModel = viewModel
            )
        }

        composable<TransferNavGraphRoutes.TransferByCardNumber> { navBackStackEntry ->

            val args = navBackStackEntry.toRoute<TransferNavGraphRoutes.TransferByCardNumber>()

            val recipientViewModel =
                navBackStackEntry.sharedKoinViewModel<RecipientViewModel>(navController)

            val viewModel = koinViewModel<TransferByCardViewModel> {
                parametersOf(
                    TransferByCardViewModelArgs(
                        cardId = args.cardId,
                        onClickBack = { navController.navigateUp() },
                        onClickRecipient = {
                            navController.navigate(TransferNavGraphRoutes.EditSearchRecipient) {
                                launchSingleTop = true
                            }
                        },
                        onClickEditRecipient = {

                        }
                    )
                )
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
    }
}