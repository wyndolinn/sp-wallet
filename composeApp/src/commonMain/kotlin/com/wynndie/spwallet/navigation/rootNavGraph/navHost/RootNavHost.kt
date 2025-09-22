package com.wynndie.spwallet.navigation.rootNavGraph.navHost

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.wynndie.spwallet.navigation.HomeNavGraphRoutes
import com.wynndie.spwallet.navigation.Route
import com.wynndie.spwallet.navigation.rootNavGraph.editNavGraph
import com.wynndie.spwallet.navigation.rootNavGraph.homeNavGraph
import com.wynndie.spwallet.navigation.rootNavGraph.transferNavGraph

@Composable
fun RootNavHost(
    startDestination: Route
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        homeNavGraph(navController)
        transferNavGraph(navController)
        editNavGraph(navController)
    }
}