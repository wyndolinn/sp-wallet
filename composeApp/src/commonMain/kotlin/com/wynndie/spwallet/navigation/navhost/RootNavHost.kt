package com.wynndie.spwallet.navigation.navhost

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.wynndie.spwallet.navigation.WalletNavGraph
import com.wynndie.spwallet.navigation.graph.walletFeature

@Composable
fun RootNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = WalletNavGraph.RootGraph
    ) {
        walletFeature(navController = navController)
    }
}