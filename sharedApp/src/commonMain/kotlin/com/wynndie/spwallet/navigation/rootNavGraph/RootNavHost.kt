package com.wynndie.spwallet.navigation.rootNavGraph

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.wynndie.spwallet.navigation.Route
import com.wynndie.spwallet.navigation.rootNavGraph.graphs.editNavGraph
import com.wynndie.spwallet.navigation.rootNavGraph.graphs.homeNavGraph
import com.wynndie.spwallet.navigation.rootNavGraph.graphs.transferNavGraph

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