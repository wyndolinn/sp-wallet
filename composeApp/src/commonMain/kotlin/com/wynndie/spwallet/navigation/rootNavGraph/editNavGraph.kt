package com.wynndie.spwallet.navigation.rootNavGraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.wynndie.spwallet.navigation.EditNavGraphRoutes
import com.wynndie.spwallet.sharedFeature.edit.presentation.screens.customCard.CustomCardScreenRoot
import com.wynndie.spwallet.sharedFeature.edit.presentation.screens.customCard.CustomCardViewModel
import com.wynndie.spwallet.sharedFeature.edit.presentation.screens.customCard.CustomCardViewModelArgs
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.editNavGraph(
    navController: NavController
) {

    navigation<EditNavGraphRoutes.EditNavGraph>(
        startDestination = EditNavGraphRoutes.CustomCard()
    ) {

        composable<EditNavGraphRoutes.CustomCard> { navBackStackEntry ->
            val args = navBackStackEntry.toRoute<EditNavGraphRoutes.CustomCard>()
            val viewModel = koinViewModel<CustomCardViewModel> {
                parametersOf(
                    CustomCardViewModelArgs(
                        cardId = args.cardId,
                        onClickBack = { navController.navigateUp() }
                    )
                )
            }

            CustomCardScreenRoot(
                viewModel = viewModel
            )
        }
    }
}