package com.jonandpaul.jonandpaul.ui.screens.home

import com.jonandpaul.jonandpaul.domain.model.CartProduct
import com.jonandpaul.jonandpaul.domain.model.Product

data class HomeState(
    val products: List<Product> = emptyList(),
    val cartProducts: List<CartProduct> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
