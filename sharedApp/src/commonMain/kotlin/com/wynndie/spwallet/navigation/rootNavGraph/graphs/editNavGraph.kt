package com.wynndie.spwallet.navigation.rootNavGraph.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.wynndie.spwallet.navigation.ObserveNavEvent
import com.wynndie.spwallet.navigation.Route
import com.wynndie.spwallet.sharedFeature.edit.presentation.screens.customCard.CustomCardNavEvent
import com.wynndie.spwallet.sharedFeature.edit.presentation.screens.customCard.CustomCardParams
import com.wynndie.spwallet.sharedFeature.edit.presentation.screens.customCard.CustomCardScreenRoot
import com.wynndie.spwallet.sharedFeature.edit.presentation.screens.customCard.CustomCardViewModel
import com.wynndie.spwallet.sharedFeature.edit.presentation.screens.recipients.RecipientsNavEvent
import com.wynndie.spwallet.sharedFeature.edit.presentation.screens.recipients.RecipientsScreenRoot
import com.wynndie.spwallet.sharedFeature.edit.presentation.screens.recipients.RecipientsViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.editNavGraph(
    navController: NavController
) {
    navigation<Route.EditNavGraph>(
        startDestination = Route.EditNavGraph.CustomCard()
    ) {
        composable<Route.EditNavGraph.CustomCard> { navBackStackEntry ->
            ObserveNavEvent<CustomCardNavEvent> { navEvent ->
                when (navEvent) {
                    CustomCardNavEvent.NavigateBack -> {
                        navController.navigateUp()
                    }
                }
            }

            val args = navBackStackEntry.toRoute<Route.EditNavGraph.CustomCard>()
            CustomCardScreenRoot(
                viewModel = koinViewModel<CustomCardViewModel> {
                    parametersOf(CustomCardParams(args.cardId))
                }
            )
        }

        composable<Route.EditNavGraph.Recipients> {
            ObserveNavEvent<RecipientsNavEvent> { navEvent ->
                when (navEvent) {
                    RecipientsNavEvent.NavigateBack -> {
                        navController.navigateUp()
                    }

                    is RecipientsNavEvent.NavigateToTransfer -> {
                        navController.navigate(
                            Route.TransferNavGraph.TransferByCardNumber(
                                recipientNumber = navEvent.number
                            )
                        )
                    }
                }
            }

            RecipientsScreenRoot(
                viewModel = koinViewModel<RecipientsViewModel>()
            )
        }
    }
}