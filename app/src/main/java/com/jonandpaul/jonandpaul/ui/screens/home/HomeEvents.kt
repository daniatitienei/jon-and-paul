package com.jonandpaul.jonandpaul.ui.screens.home

import com.jonandpaul.jonandpaul.domain.model.Product

sealed class HomeEvents {
    object OnFavoritesClick : HomeEvents()
    object OnAccountClick : HomeEvents()

    object RevealBackdrop : HomeEvents()
    object ConcealBackdrop : HomeEvents()

    data class OnProductClick(var product: Product) : HomeEvents()

    /* Cart */
    object ExpandBottomSheet : HomeEvents()
    object CollapseBottomSheet : HomeEvents()

    object ShowModalBottomSheet : HomeEvents()
    object HideModalBottomSheet : HomeEvents()
}
