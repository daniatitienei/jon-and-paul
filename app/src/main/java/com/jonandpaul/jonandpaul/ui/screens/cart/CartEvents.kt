package com.jonandpaul.jonandpaul.ui.screens.cart

import com.jonandpaul.jonandpaul.CartItemEntity
import com.jonandpaul.jonandpaul.domain.model.ShippingDetails

sealed class CartEvents {
    object ShowModalBottomSheet : CartEvents()
    object HideModalBottomSheet : CartEvents()

    data class OnDeleteProduct(val id: Long) : CartEvents()
    data class OnUpdateQuantity(val id: Long, val quantity: Long) : CartEvents()

    object OnAddressClick : CartEvents()
    data class OnOrderClick(val items: List<CartItemEntity>, val shippingDetails: ShippingDetails) : CartEvents()

    object OnNavigationClick : CartEvents()
    data class OnProductClick(val product: CartItemEntity) : CartEvents()
}
