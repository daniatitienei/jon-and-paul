package com.jonandpaul.jonandpaul.ui.screens.cart

sealed class CartEvents {
    object ShowModalBottomSheet : CartEvents()
    object HideModalBottomSheet : CartEvents()

    data class OnDeleteProduct(val id: Long) : CartEvents()
    data class OnUpdateQuantity(val id: Long, val quantity: Long) : CartEvents()

    object OnAddressClick : CartEvents()
    object OnCreateCreditCardClick : CartEvents()
    object OnOrderClick : CartEvents()

    object OnNavigationClick : CartEvents()
}
