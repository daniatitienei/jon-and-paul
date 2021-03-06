package com.jonandpaul.jonandpaul.ui.screens.home

import com.jonandpaul.jonandpaul.domain.model.Product

sealed class HomeEvents {
    object OnFavoritesClick : HomeEvents()
    object OnAccountClick : HomeEvents()
    object OnCartClick : HomeEvents()

    object RevealBackdrop : HomeEvents()
    object ConcealBackdrop : HomeEvents()

    data class OnProductClick(val product: Product) : HomeEvents()

    data class OnFavoriteClick(val isFavorite: Boolean, val product: Product) : HomeEvents()

    /* Cart */
    object ShowModalBottomSheet : HomeEvents()
    object HideModalBottomSheet : HomeEvents()
}
