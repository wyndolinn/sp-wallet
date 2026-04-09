package com.wynndie.spwallet.navigation.rootNavGraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.wynndie.spwallet.navigation.EditNavGraphRoutes
import com.wynndie.spwallet.navigation.ObserveNavEvent
import com.wynndie.spwallet.navigation.koinViewModelWithArgs
import com.wynndie.spwallet.sharedFeature.edit.presentation.screens.customCard.CustomCardNavEvent
import com.wynndie.spwallet.sharedFeature.edit.presentation.screens.customCard.CustomCardScreenRoot
import com.wynndie.spwallet.sharedFeature.edit.presentation.screens.customCard.CustomCardViewModel
import com.wynndie.spwallet.sharedFeature.edit.presentation.screens.customCard.CustomCardViewModelArgs

fun NavGraphBuilder.editNavGraph(
    navController: NavController
) {

    navigation<EditNavGraphRoutes.EditNavGraph>(
        startDestination = EditNavGraphRoutes.CustomCard()
    ) {

        composable<EditNavGraphRoutes.CustomCard> { navBackStackEntry ->

            val args = navBackStackEntry.toRoute<EditNavGraphRoutes.CustomCard>()

            val viewModel = koinViewModelWithArgs<CustomCardViewModel>(
                CustomCardViewModelArgs(cardId = args.cardId)
            )

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