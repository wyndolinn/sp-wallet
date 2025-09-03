package com.wynndie.spwallet.navigation.rootNavGraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.wynndie.spwallet.navigation.ProfileNavGraphRoutes
import com.wynndie.spwallet.sharedFeature.profile.presentation.screen.localization.ThemeScreenRoot
import com.wynndie.spwallet.sharedFeature.profile.presentation.screen.localization.ThemeViewModel
import com.wynndie.spwallet.sharedFeature.profile.presentation.screen.localization.ThemeViewModelArgs
import com.wynndie.spwallet.sharedFeature.profile.presentation.screen.menu.MenuScreenRoot
import com.wynndie.spwallet.sharedFeature.profile.presentation.screen.menu.MenuViewModel
import com.wynndie.spwallet.sharedFeature.profile.presentation.screen.menu.MenuViewModelArgs
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.profileNavGraph(
    navController: NavController
) {

    navigation<ProfileNavGraphRoutes.ProfileNavGraph>(
        startDestination = ProfileNavGraphRoutes.Menu
    ) {

        composable<ProfileNavGraphRoutes.Menu> {

            val viewModel = koinViewModel<MenuViewModel> {
                parametersOf(
                    MenuViewModelArgs(
                        onClickBack = {
                            navController.navigateUp()
                        },
                        onClickTheme = {
                            navController.navigate(ProfileNavGraphRoutes.Theme) {
                                launchSingleTop = true
                            }
                        }
                    )
                )
            }

            MenuScreenRoot(viewModel = viewModel)
        }

        composable<ProfileNavGraphRoutes.Theme> {
            val viewModel = koinViewModel<ThemeViewModel> {
                parametersOf(
                    ThemeViewModelArgs(
                        onClickBack = {
                            navController.navigateUp()
                        }
                    )
                )
            }

            ThemeScreenRoot(viewModel = viewModel)
        }
    }
}