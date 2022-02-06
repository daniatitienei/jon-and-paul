package com.jonandpaul.jonandpaul.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jonandpaul.jonandpaul.domain.model.Order
import com.jonandpaul.jonandpaul.domain.model.Product
import com.jonandpaul.jonandpaul.ui.screens.account.AccountScreen
import com.jonandpaul.jonandpaul.ui.screens.address.ShippingDetailsScreen
import com.jonandpaul.jonandpaul.ui.screens.cart.CartScreen
import com.jonandpaul.jonandpaul.ui.screens.favorites.FavoritesScreen
import com.jonandpaul.jonandpaul.ui.screens.home.HomeScreen
import com.jonandpaul.jonandpaul.ui.screens.inspect_order.InspectOrderScreen
import com.jonandpaul.jonandpaul.ui.screens.inspect_product.InspectProductScreen
import com.jonandpaul.jonandpaul.ui.screens.latest_orders.LatestOrdersScreen
import com.jonandpaul.jonandpaul.ui.screens.order_placed.OrderPlacedScreen
import com.jonandpaul.jonandpaul.ui.theme.Black900
import com.jonandpaul.jonandpaul.ui.utils.Screens
import com.squareup.moshi.Moshi

@ExperimentalMaterial3Api
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun Navigation(
    moshi: Moshi,
    auth: FirebaseAuth,
    firestore: FirebaseFirestore
) {
    val navController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = if (auth.currentUser == null) "authenticating" else Screens.Home.route
    ) {
        composable(route = Screens.Home.route) {
            HomeScreen(
                onNavigate = { destination ->
                    navController.navigate(destination.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = "authenticating") {

            auth.signInAnonymously()
                .addOnSuccessListener {
                    firestore.collection("users").document(auth.currentUser!!.uid)
                        .set(hashMapOf("favorites" to listOf<Product>()))

                    navController.navigate(route = Screens.Home.route)
                }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(align = Alignment.Center)
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
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

        composable(
            route = Screens.Favorites.route,
            enterTransition = {
                slideIntoContainer(towards = AnimatedContentScope.SlideDirection.Left) + fadeIn(
                    tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(towards = AnimatedContentScope.SlideDirection.Right) + fadeOut(
                    tween(500)
                )
            },
            popEnterTransition = {
                slideIntoContainer(towards = AnimatedContentScope.SlideDirection.Left) + fadeIn(
                    tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(towards = AnimatedContentScope.SlideDirection.Right) + fadeOut(
                    tween(500)
                )
            }
        ) {
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
                slideIntoContainer(towards = AnimatedContentScope.SlideDirection.Left) + fadeIn(
                    tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(towards = AnimatedContentScope.SlideDirection.Right) + fadeOut(
                    tween(500)
                )
            },
            popEnterTransition = {
                slideIntoContainer(towards = AnimatedContentScope.SlideDirection.Left) + fadeIn(
                    tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(towards = AnimatedContentScope.SlideDirection.Right) + fadeOut(
                    tween(500)
                )
            }
        ) {
            ShippingDetailsScreen(
                onPopBackStack = {
                    navController.popBackStack()
                },
            )
        }

        composable(
            route = Screens.Account.route,
            enterTransition = {
                slideIntoContainer(towards = AnimatedContentScope.SlideDirection.Left) + fadeIn(
                    tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(towards = AnimatedContentScope.SlideDirection.Right) + fadeOut(
                    tween(500)
                )
            },
            popEnterTransition = {
                slideIntoContainer(towards = AnimatedContentScope.SlideDirection.Left) + fadeIn(
                    tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(towards = AnimatedContentScope.SlideDirection.Right) + fadeOut(
                    tween(500)
                )
            }
        ) {
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
                slideIntoContainer(towards = AnimatedContentScope.SlideDirection.Left) + fadeIn(
                    tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(towards = AnimatedContentScope.SlideDirection.Right) + fadeOut(
                    tween(500)
                )
            },
            popEnterTransition = {
                slideIntoContainer(towards = AnimatedContentScope.SlideDirection.Left) + fadeIn(
                    tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(towards = AnimatedContentScope.SlideDirection.Right) + fadeOut(
                    tween(500)
                )
            }
        ) { backStackEntry ->
            val productJson = backStackEntry.arguments?.getString("product")
            val jsonAdapter = moshi.adapter(Product::class.java).lenient()
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
            route = Screens.OrderPlaced.route,
            enterTransition = {
                slideIntoContainer(towards = AnimatedContentScope.SlideDirection.Left) + fadeIn(
                    tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(towards = AnimatedContentScope.SlideDirection.Right) + fadeOut(
                    tween(500)
                )
            },
            popEnterTransition = {
                slideIntoContainer(towards = AnimatedContentScope.SlideDirection.Left) + fadeIn(
                    tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(towards = AnimatedContentScope.SlideDirection.Right) + fadeOut(
                    tween(500)
                )
            }
        ) {
            OrderPlacedScreen(
                onNavigate = { destination ->
                    navController.navigate(destination.route) {
                        launchSingleTop = true

                        popUpTo(Screens.OrderPlaced.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(
            route = Screens.InspectOrder.route,
            enterTransition = {
                slideIntoContainer(towards = AnimatedContentScope.SlideDirection.Left) + fadeIn(
                    tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(towards = AnimatedContentScope.SlideDirection.Right) + fadeOut(
                    tween(500)
                )
            },
            popEnterTransition = {
                slideIntoContainer(towards = AnimatedContentScope.SlideDirection.Left) + fadeIn(
                    tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(towards = AnimatedContentScope.SlideDirection.Right) + fadeOut(
                    tween(500)
                )
            }
        ) {
            InspectOrderScreen(
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
            route = Screens.LatestOrders.route,
            enterTransition = {
                slideIntoContainer(towards = AnimatedContentScope.SlideDirection.Left) + fadeIn(
                    tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(towards = AnimatedContentScope.SlideDirection.Right) + fadeOut(
                    tween(500)
                )
            },
            popEnterTransition = {
                slideIntoContainer(towards = AnimatedContentScope.SlideDirection.Left) + fadeIn(
                    tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(towards = AnimatedContentScope.SlideDirection.Right) + fadeOut(
                    tween(500)
                )
            }
        ) {
            LatestOrdersScreen(
                onNavigate = { destination ->
                    navController.navigate(destination.route) {
                        launchSingleTop = true
                    }
                },
                onPopBackStack = {
                    navController.popBackStack()
                },
            )
        }
    }
}