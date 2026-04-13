package com.wynndie.spwallet.navigation.rootNavGraph

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
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.editNavGraph(
    navController: NavController
) {

    navigation<Route.EditNavGraph>(
        startDestination = Route.EditNavGraph.CustomCard()
    ) {

        composable<Route.EditNavGraph.CustomCard> { navBackStackEntry ->

            val args = navBackStackEntry.toRoute<Route.EditNavGraph.CustomCard>()

            val viewModel = koinViewModel<CustomCardViewModel> {
                parametersOf(CustomCardParams(args.cardId))
            }

            ObserveNavEvent<CustomCardNavEvent> { navEvent ->
                when (navEvent) {
                    CustomCardNavEvent.OnClickBack -> {
                        navController.navigateUp()
                    }
                }
            }

            CustomCardScreenRoot(
                viewModel = viewModel
            )
        }
    }
}