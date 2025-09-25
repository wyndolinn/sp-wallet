package com.wynndie.spwallet.navigation.rootNavGraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.wynndie.spwallet.navigation.EditNavGraphRoutes
import com.wynndie.spwallet.navigation.HomeNavGraphRoutes
import com.wynndie.spwallet.navigation.ObserveNavEvent
import com.wynndie.spwallet.navigation.TransferNavGraphRoutes
import com.wynndie.spwallet.sharedFeature.home.presentation.screens.home.HomeNavEvent
import com.wynndie.spwallet.sharedFeature.home.presentation.screens.home.HomeScreenRoot
import com.wynndie.spwallet.sharedFeature.home.presentation.screens.home.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.homeNavGraph(
    navController: NavController
) {

    navigation<HomeNavGraphRoutes.HomeNavGraph>(
        startDestination = HomeNavGraphRoutes.Home
    ) {

        composable<HomeNavGraphRoutes.Home> {

            val viewModel = koinViewModel<HomeViewModel>()

            ObserveNavEvent<HomeNavEvent> { navEvent ->
                when (navEvent) {
                    is HomeNavEvent.OnClickCustomCard -> {
                        navController.navigate(EditNavGraphRoutes.CustomCard(navEvent.cardId)) {
                            launchSingleTop = true
                        }
                    }

                    is HomeNavEvent.OnClickTransferByCard -> {
                        navController.navigate(TransferNavGraphRoutes.SearchRecipient(navEvent.cardId)) {
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