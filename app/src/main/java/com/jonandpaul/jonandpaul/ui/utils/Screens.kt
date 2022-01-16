package com.jonandpaul.jonandpaul.ui.utils

sealed class Screens(val route: String) {
    object Home : Screens(route = "home")
    object Account : Screens(route = "account")
    object Cart : Screens(route = "cart")
    object Favorites : Screens(route = "favorites")
}
