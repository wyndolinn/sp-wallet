package com.wynndie.spwallet.navigation.rootNavGraph.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.wynndie.spwallet.navigation.ObserveNavEvent
import com.wynndie.spwallet.navigation.Route
import com.wynndie.spwallet.sharedFeature.home.presentation.HomeNavEvent
import com.wynndie.spwallet.sharedFeature.home.presentation.HomeScreenRoot
import com.wynndie.spwallet.sharedFeature.home.presentation.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.homeNavGraph(
    navController: NavController
) {

    navigation<Route.HomeNavGraph>(
        startDestination = Route.HomeNavGraph.Home
    ) {

        composable<Route.HomeNavGraph.Home> {

            val viewModel = koinViewModel<HomeViewModel>()

            ObserveNavEvent<HomeNavEvent> { navEvent ->
                when (navEvent) {
                    is HomeNavEvent.OnClickCustomCard -> {
                        navController.navigate(
                            Route.EditNavGraph.CustomCard(navEvent.cardId)
                        ) {
                            launchSingleTop = true
                        }
                    }

                    is HomeNavEvent.OnClickTransferByCard -> {
                        navController.navigate(
                            Route.TransferNavGraph.SearchRecipient(navEvent.cardId)
                        ) {
                            launchSingleTop = true
                        }
                    }

                    is HomeNavEvent.OnClickTransferBetweenCards -> {
                        navController.navigate(
                            Route.TransferNavGraph.TransferBetweenCards(navEvent.cardId)
                        ) {
                            launchSingleTop = true
                        }
                    }
                }
            }

            HomeScreenRoot(
                viewModel = viewModel
            )
        }
    }
}