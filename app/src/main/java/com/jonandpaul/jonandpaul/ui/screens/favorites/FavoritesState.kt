package com.jonandpaul.jonandpaul.ui.screens.favorites

import com.jonandpaul.jonandpaul.domain.model.Product

data class FavoritesState(
    val favorites: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
