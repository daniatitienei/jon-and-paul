package com.jonandpaul.jonandpaul.ui.screens.cart

import com.jonandpaul.jonandpaul.domain.model.CartProduct

sealed class CartEvents {
    object ShowModalBottomSheet : CartEvents()
    object HideModalBottomSheet : CartEvents()

    data class OnDeleteProduct(val item: CartProduct) : CartEvents()
    object OnAddressClick : CartEvents()
    object OnCreditCardClick : CartEvents()
    object OnOrderClick : CartEvents()
    object OnNavigationClick : CartEvents()
}
