package com.jonandpaul.jonandpaul.ui.screens.inspect_order

import com.jonandpaul.jonandpaul.domain.model.CartItem

sealed class InspectOrderEvents {
    object OnNavigationIconClick : InspectOrderEvents()
    data class OnProductClick(val item: CartItem) : InspectOrderEvents()
}
