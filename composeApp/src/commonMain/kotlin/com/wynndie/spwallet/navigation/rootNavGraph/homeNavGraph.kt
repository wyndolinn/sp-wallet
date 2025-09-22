package com.wynndie.spwallet.navigation.rootNavGraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.wynndie.spwallet.navigation.EditNavGraphRoutes
import com.wynndie.spwallet.navigation.HomeNavGraphRoutes
import com.wynndie.spwallet.navigation.TransferNavGraphRoutes
import com.wynndie.spwallet.sharedFeature.home.presentation.screen.home.HomeScreenRoot
import com.wynndie.spwallet.sharedFeature.home.presentation.screen.home.HomeViewModel
import com.wynndie.spwallet.sharedFeature.home.presentation.screen.home.HomeViewModelArgs
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
                        onClickCashCard = { cardId ->
                            navController.navigate(EditNavGraphRoutes.CustomCard(cardId)) {
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
    }
}