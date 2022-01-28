package com.jonandpaul.jonandpaul.ui.utils

sealed class Screens(val route: String) {
    object Home : Screens(route = "home")
    object Cart : Screens(route = "cart")
    object Favorites : Screens(route = "favorites")
    object Account : Screens(route = "account")

    object InspectProduct : Screens(route = "inspect_product/{product}")

    object Address : Screens(route = "address")
    object AddCreditCard : Screens(route = "add_credit_card")
}
