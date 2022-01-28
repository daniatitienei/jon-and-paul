package com.jonandpaul.jonandpaul.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
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
import com.jonandpaul.jonandpaul.ui.screens.address.AddressScreen
import com.jonandpaul.jonandpaul.ui.screens.cart.CartScreen
import com.jonandpaul.jonandpaul.ui.screens.favorites.FavoritesScreen
import com.jonandpaul.jonandpaul.ui.screens.home.HomeScreen
import com.jonandpaul.jonandpaul.ui.screens.inspect_product.InspectProductScreen
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

        composable(
            route = Screens.Cart.route,
            enterTransition = {
                when (this.initialState.destination.route) {
                    Screens.Home.route -> {
                        slideInVertically(initialOffsetY = { 1000 }) + fadeIn(tween(500))
                    }
                    else -> fadeIn(tween(500))
                }
            },
            exitTransition = {
                when (this.targetState.destination.route) {
                    Screens.Home.route -> {
                        slideOutVertically(targetOffsetY = { 1000 }) + fadeOut(tween(500))
                    }
                    else -> fadeOut(tween(500))
                }
            },
            popEnterTransition = {
                when (this.initialState.destination.route) {
                    Screens.Home.route -> {
                        slideInVertically(initialOffsetY = { 1000 }) + fadeIn(tween(500))
                    }
                    else -> fadeIn(tween(500))
                }
            },
            popExitTransition = {
                when (this.targetState.destination.route) {
                    Screens.Home.route -> {
                        slideOutVertically(targetOffsetY = { 1000 }) + fadeOut(tween(500))
                    }
                    else -> fadeOut(tween(500))
                }
            }
        ) {
            CartScreen(
                onNavigate = { destination ->
                    navController.navigate(destination.route) {
                        launchSingleTop = true
                    }
                },
                onPopBackStack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Screens.Favorites.route) {
            FavoritesScreen(
                onNavigate = { destination ->
                    navController.navigate(destination.route) {
                        launchSingleTop = true
                    }
                },
                onPopBackStack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screens.Address.route,
            enterTransition = {
                slideIntoContainer(towards = AnimatedContentScope.SlideDirection.Left)
            },
            exitTransition = {
                slideOutOfContainer(towards = AnimatedContentScope.SlideDirection.Right)
            },
            popEnterTransition = {
                slideIntoContainer(towards = AnimatedContentScope.SlideDirection.Left)
            },
            popExitTransition = {
                slideOutOfContainer(towards = AnimatedContentScope.SlideDirection.Right)
            }
        ) {
            AddressScreen(
                onPopBackStack = {
                    navController.popBackStack()
                },
            )
        }

        composable(route = Screens.Account.route) {
            AccountScreen(
                onNavigate = { destination ->
                    navController.navigate(destination.route) {
                        launchSingleTop = true
                    }
                },
                onPopBackStack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screens.InspectProduct.route,
            enterTransition = {
                slideIntoContainer(towards = AnimatedContentScope.SlideDirection.Left)
            },
            exitTransition = {
                slideOutOfContainer(towards = AnimatedContentScope.SlideDirection.Right)
            },
            popEnterTransition = {
                slideIntoContainer(towards = AnimatedContentScope.SlideDirection.Left)
            },
            popExitTransition = {
                slideOutOfContainer(towards = AnimatedContentScope.SlideDirection.Right)
            }
        ) { backStackEntry ->
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
    }
}