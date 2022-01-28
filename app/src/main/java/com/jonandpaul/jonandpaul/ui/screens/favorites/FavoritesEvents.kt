package com.jonandpaul.jonandpaul.ui.screens.favorites

import com.jonandpaul.jonandpaul.domain.model.Product

sealed class FavoritesEvents {
    object OnNavigationClick : FavoritesEvents()
    object OnProductClick : FavoritesEvents()
    data class OnFavoriteClick(val product: Product) : FavoritesEvents()
}
