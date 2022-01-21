package com.jonandpaul.jonandpaul.ui.utils

sealed class Screens(val route: String) {
    object Home : Screens(route = "home")
    object Cart : Screens(route = "cart")
    object Favorites : Screens(route = "favorites")

    object Register : Screens("register")
    object Login : Screens("login")
    object InspectProduct : Screens(route = "inspect_product/{product}")
}
