package com.wynndie.spwallet.navigation.rootNavGraph.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.wynndie.spwallet.navigation.ObserveNavEvent
import com.wynndie.spwallet.navigation.Route
import com.wynndie.spwallet.sharedFeature.home.presentation.screens.auth.AuthNavEvent
import com.wynndie.spwallet.sharedFeature.home.presentation.screens.auth.AuthScreenRoot
import com.wynndie.spwallet.sharedFeature.home.presentation.screens.auth.AuthViewModel
import com.wynndie.spwallet.sharedFeature.home.presentation.screens.home.HomeNavEvent
import com.wynndie.spwallet.sharedFeature.home.presentation.screens.home.HomeScreenRoot
import com.wynndie.spwallet.sharedFeature.home.presentation.screens.home.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.homeNavGraph(
    navController: NavController
) {
    navigation<Route.HomeNavGraph>(
        startDestination = Route.HomeNavGraph.Home
    ) {
        composable<Route.HomeNavGraph.Home> {
            ObserveNavEvent<HomeNavEvent> { event ->
                when (event) {
                    HomeNavEvent.NavigateToAuthCard -> {
                        navController.navigate(Route.HomeNavGraph.Auth) {
                            launchSingleTop = true
                        }
                    }

                    HomeNavEvent.NavigateToRecipients -> {
                        navController.navigate(Route.EditNavGraph.Recipients) {
                            launchSingleTop = true
                        }
                    }

                    is HomeNavEvent.NavigateToCustomCard -> {
                        navController.navigate(
                            Route.EditNavGraph.CustomCard(event.cardId)
                        ) {
                            launchSingleTop = true
                        }
                    }

                    is HomeNavEvent.NavigateToTransferByCard -> {
                        navController.navigate(
                            Route.TransferNavGraph.SearchRecipient(event.cardId)
                        ) {
                            launchSingleTop = true
                        }
                    }

                    is HomeNavEvent.NavigateToTransferBetweenCards -> {
                        navController.navigate(
                            Route.TransferNavGraph.TransferBetweenCards(event.cardId)
                        ) {
                            launchSingleTop = true
                        }
                    }
                }
            }

            HomeScreenRoot(
                viewModel = koinViewModel<HomeViewModel>()
            )
        }

        composable<Route.HomeNavGraph.Auth> {
            ObserveNavEvent<AuthNavEvent> { event ->
                when (event) {
                    AuthNavEvent.NavigateBack -> {
                        navController.navigateUp()
                    }
                }
            }

            AuthScreenRoot(
                viewModel = koinViewModel<AuthViewModel>()
            )
        }
    }
}