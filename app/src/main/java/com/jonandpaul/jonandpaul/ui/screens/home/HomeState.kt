package com.jonandpaul.jonandpaul.ui.screens.home

import com.jonandpaul.jonandpaul.ui.domain.Product

data class HomeState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
