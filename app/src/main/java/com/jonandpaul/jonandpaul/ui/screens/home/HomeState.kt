package com.jonandpaul.jonandpaul.ui.screens.home

import com.jonandpaul.jonandpaul.domain.model.CartItem
import com.jonandpaul.jonandpaul.domain.model.Product

data class HomeState(
    val products: List<Product> = emptyList(),
    val favorites: List<Product> = emptyList(),
    val cartItems: List<CartItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
