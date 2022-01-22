package com.jonandpaul.jonandpaul.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.NavHost
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.jonandpaul.jonandpaul.domain.model.Product
import com.jonandpaul.jonandpaul.ui.screens.account.AccountScreen
import com.jonandpaul.jonandpaul.ui.screens.home.HomeScreen
import com.jonandpaul.jonandpaul.ui.screens.inspect_product.InspectProductScreen
import com.jonandpaul.jonandpaul.ui.screens.login.LoginScreen
import com.jonandpaul.jonandpaul.ui.screens.register.RegisterScreen
import com.jonandpaul.jonandpaul.ui.utils.Screens
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun Navigation(moshi: Moshi) {

    val navController = rememberAnimatedNavController()

    AnimatedNavHost(navController = navController, startDestination = Screens.Home.route) {
        composable(route = Screens.Home.route) {
            HomeScreen(
                onNavigate = { destination ->
                    navController.navigate(destination.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = Screens.Account.route) {
            AccountScreen()
        }

        composable(route = Screens.InspectProduct.route) { backStackEntry ->
            val productJson = backStackEntry.arguments?.getString("product")
            val jsonAdapter = moshi.adapter(Product::class.java)
            val productObject = jsonAdapter.fromJson(productJson!!)

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

        composable(
            route = Screens.Login.route
        ) {
            LoginScreen(
                onNavigate = { destination ->
                    navController.navigate(destination.route) {
                        launchSingleTop = true

                        popUpTo(Screens.Login.route) {
                            inclusive = true
                        }
                    }
                },
                onPopBackStack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screens.Register.route,
            enterTransition = {
                slideInVertically(initialOffsetY = { 1000 })
            },
            popEnterTransition = {
                slideInVertically(initialOffsetY = { 1000 })
            },
            exitTransition = {
                slideOutVertically(targetOffsetY = { 1000 })
            },
            popExitTransition = {
                slideOutVertically(targetOffsetY = { 1000 })
            }
        ) {
            RegisterScreen(
                onNavigate = { destination ->
                    navController.navigate(destination.route) {
                        launchSingleTop = true

                        popUpTo(Screens.Register.route) {
                            inclusive = true
                        }
                    }
                },
                onPopBackStack = {
                    navController.popBackStack()
                }
            )
        }
    }
}