package com.jonandpaul.jonandpaul.ui.screens.favorites

import com.jonandpaul.jonandpaul.domain.model.Product

sealed class FavoritesEvents {
    object OnNavigationClick : FavoritesEvents()
    data class OnProductClick(val product: Product) : FavoritesEvents()
    data class OnFavoriteClick(val product: Product) : FavoritesEvents()
}
