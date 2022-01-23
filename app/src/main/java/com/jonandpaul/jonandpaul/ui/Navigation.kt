package com.jonandpaul.jonandpaul.ui

import androidx.compose.animation.*
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

        composable(
            route = Screens.Login.route,
            enterTransition = {
                when (this.initialState.destination.route) {
                    Screens.Register.route -> {
                        slideIntoContainer(towards = AnimatedContentScope.SlideDirection.Left)
                    }
                    else -> slideIntoContainer(towards = AnimatedContentScope.SlideDirection.Down)
                }
            },
            exitTransition = {
                when (this.targetState.destination.route) {
                    Screens.Register.route -> {
                        slideOutOfContainer(towards = AnimatedContentScope.SlideDirection.Right)
                    }
                    else -> slideOutOfContainer(towards = AnimatedContentScope.SlideDirection.Up)
                }
            },
            popEnterTransition = {
                when (this.initialState.destination.route) {
                    Screens.Register.route -> {
                        slideIntoContainer(towards = AnimatedContentScope.SlideDirection.Left)
                    }
                    else -> slideIntoContainer(towards = AnimatedContentScope.SlideDirection.Down)
                }
            },
            popExitTransition = {
                when (this.targetState.destination.route) {
                    Screens.Register.route -> {
                        slideOutOfContainer(towards = AnimatedContentScope.SlideDirection.Right)
                    }
                    else -> slideOutOfContainer(towards = AnimatedContentScope.SlideDirection.Up)
                }
            }
        ) {
            LoginScreen(
                onNavigate = { destination ->
                    navController.navigate(destination.route) {
                        launchSingleTop = true

                        destination.popUpTo?.let { popUpToRoute ->
                            popUpTo(popUpToRoute) {
                                inclusive = true
                            }
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
                when (this.initialState.destination.route) {
                    Screens.Login.route -> {
                        slideIntoContainer(towards = AnimatedContentScope.SlideDirection.Left)
                    }
                    else -> slideIntoContainer(towards = AnimatedContentScope.SlideDirection.Down)
                }
            },
            exitTransition = {
                when (this.targetState.destination.route) {
                    Screens.Login.route -> {
                        slideOutOfContainer(towards = AnimatedContentScope.SlideDirection.Right)
                    }
                    else -> slideOutOfContainer(towards = AnimatedContentScope.SlideDirection.Up)
                }
            },
            popEnterTransition = {
                when (this.initialState.destination.route) {
                    Screens.Login.route -> {
                        slideIntoContainer(towards = AnimatedContentScope.SlideDirection.Left)
                    }
                    else -> slideIntoContainer(towards = AnimatedContentScope.SlideDirection.Down)
                }
            },
            popExitTransition = {
                when (this.targetState.destination.route) {
                    Screens.Login.route -> {
                        slideOutOfContainer(towards = AnimatedContentScope.SlideDirection.Right)
                    }
                    else -> slideOutOfContainer(towards = AnimatedContentScope.SlideDirection.Up)
                }
            }
        ) {
            RegisterScreen(
                onNavigate = { destination ->
                    navController.navigate(destination.route) {
                        launchSingleTop = true

                        destination.popUpTo?.let { popUpToRoute ->
                            popUpTo(popUpToRoute) {
                                inclusive = true
                            }
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