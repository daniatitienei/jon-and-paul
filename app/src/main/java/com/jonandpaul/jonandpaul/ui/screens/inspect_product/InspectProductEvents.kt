package com.jonandpaul.jonandpaul.ui.screens.inspect_product

import com.jonandpaul.jonandpaul.domain.model.Product

sealed class InspectProductEvents {
    object OnPopBackStack: InspectProductEvents()
    data class OnProductClick(var product: Product): InspectProductEvents()
    data class OnAddToCartClick(val product: Product) : InspectProductEvents()
}