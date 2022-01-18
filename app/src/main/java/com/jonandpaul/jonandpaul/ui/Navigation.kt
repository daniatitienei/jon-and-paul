package com.jonandpaul.jonandpaul.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jonandpaul.jonandpaul.domain.model.Product
import com.jonandpaul.jonandpaul.ui.screens.home.HomeScreen
import com.jonandpaul.jonandpaul.ui.screens.inspect_product.InspectProductScreen
import com.jonandpaul.jonandpaul.ui.utils.Screens
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun Navigation(moshi: Moshi) {

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

        composable(route = Screens.InspectProduct.route) { backStackEntry ->
            val productJson = backStackEntry.arguments?.getString("product")
            val jsonAdapter = moshi.adapter(Product::class.java)
            val productObject = jsonAdapter.fromJson(productJson)

            productObject?.let { product ->
                InspectProductScreen(
                    onNavigate = { destination ->
                        navController.navigate(destination.route) {
                            launchSingleTop = true
                        }
                    },
                    onPopBackStack = {
                        navController.popBackStack()
                    },
                    product = product
                )
            }
        }
    }
}