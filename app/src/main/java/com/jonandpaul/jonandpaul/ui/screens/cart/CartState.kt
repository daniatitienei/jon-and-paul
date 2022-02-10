package com.jonandpaul.jonandpaul.ui.screens.cart

import com.jonandpaul.jonandpaul.domain.model.CartItem

data class CartState(
    val items: List<CartItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)