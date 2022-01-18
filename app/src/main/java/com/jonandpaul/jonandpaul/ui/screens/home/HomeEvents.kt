package com.jonandpaul.jonandpaul.ui.screens.home

import com.jonandpaul.jonandpaul.domain.model.Product

sealed class HomeEvents {
    object OnSearchClick: HomeEvents()
    object OnBagClick: HomeEvents()
    object OnFavoritesClick: HomeEvents()
    object OnAccountClick: HomeEvents()
    data class OnProductClick(var product: Product): HomeEvents()
}
