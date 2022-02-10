package com.jonandpaul.jonandpaul.ui.screens.cart

import com.jonandpaul.jonandpaul.domain.model.CartItem
import com.jonandpaul.jonandpaul.domain.model.ShippingDetails

sealed class CartEvents {
    object ShowModalBottomSheet : CartEvents()
    object HideModalBottomSheet : CartEvents()

    data class OnDeleteProduct(val id: Int) : CartEvents()
    data class OnUpdateQuantity(val id: Int, val quantity: Int) : CartEvents()

    object OnAddressClick : CartEvents()
    data class OnOrderClick(val shippingDetails: ShippingDetails) : CartEvents()

    object OnNavigationClick : CartEvents()
    data class OnProductClick(val item: CartItem) : CartEvents()
}
