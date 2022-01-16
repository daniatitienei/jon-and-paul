package com.jonandpaul.jonandpaul.ui.screens.home

sealed class HomeEvents {
    object OnSearchClick: HomeEvents()
    object OnBagClick: HomeEvents()
    object OnFavoritesClick: HomeEvents()
    object OnAccountClick: HomeEvents()
    object OnProductClick: HomeEvents()
}
