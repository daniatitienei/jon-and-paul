package com.jonandpaul.jonandpaul.ui.utils

sealed class Screens(val route: String) {
    object Home : Screens(route = "home")
    object Cart : Screens(route = "cart")
    object Favorites : Screens(route = "favorites")
    object Account : Screens(route = "account")

    object InspectProduct : Screens(route = "inspect_product/?product={product}")
    object Address : Screens(route = "address")
    object OrderPlaced : Screens(route = "order_placed")
    object LatestOrders : Screens(route = "latest_orders")
    object InspectOrder : Screens(route = "inspect_order/?orderId={orderId}")
}
