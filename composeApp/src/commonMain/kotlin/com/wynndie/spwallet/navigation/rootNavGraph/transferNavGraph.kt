package com.wynndie.spwallet.navigation.rootNavGraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.wynndie.spwallet.navigation.TransferNavGraphRoutes
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

            val viewModel = koinViewModel<SearchRecipientViewModel> {
                parametersOf(
                    SearchRecipientViewModelArgs(
                        onClickBack = { navController.navigateUp() },
                        onClickRecipient = { recipientId, cardNumber ->
                            navController.navigate(
                                TransferNavGraphRoutes.TransferByCardNumber(
                                    cardId = args.cardId,
                                    recipientId = recipientId,
                                    recipientCardNumber = cardNumber
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

        composable<TransferNavGraphRoutes.TransferByCardNumber> { navBackStackEntry ->

            val args = navBackStackEntry.toRoute<TransferNavGraphRoutes.TransferByCardNumber>()

            val viewModel = koinViewModel<TransferByCardViewModel> {
                parametersOf(
                    TransferByCardViewModelArgs(
                        cardId = args.cardId,
                        recipientId = args.recipientId,
                        recipientCardNumber = args.recipientCardNumber,
                        onClickBack = { navController.navigateUp() },
                        onClickRecipient = {

                        },
                        onClickEditRecipient = {

                        }
                    )
                )
            }

            TransferByCardScreenRoot(
                viewModel = viewModel
            )
        }
    }
}