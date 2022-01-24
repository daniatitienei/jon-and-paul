package com.jonandpaul.jonandpaul.ui.screens.cart

import com.jonandpaul.jonandpaul.domain.model.CartProduct

data class CartState(
    val isLoading: Boolean = false,
    val cartItems: List<CartProduct> = emptyList(),
    val error: String? = null
)
