package com.jonandpaul.jonandpaul.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jonandpaul.jonandpaul.ui.screens.home.HomeScreen
import com.jonandpaul.jonandpaul.ui.screens.inspect_product.InspectProductScreen
import com.jonandpaul.jonandpaul.ui.utils.Screens

@ExperimentalFoundationApi
@Composable
fun Navigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.Home.route) {
        composable(route = Screens.Home.route) {
            HomeScreen(
                onNavigate = { destination ->
                    navController.navigate(destination.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = Screens.InspectProduct.route) {
            InspectProductScreen()
        }
    }
}