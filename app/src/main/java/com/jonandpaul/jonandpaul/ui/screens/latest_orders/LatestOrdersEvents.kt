package com.jonandpaul.jonandpaul.ui.screens.latest_orders

import com.jonandpaul.jonandpaul.domain.model.Order

sealed class LatestOrdersEvents {
    object OnNavigationIconClick : LatestOrdersEvents()
    data class OnOrderClick(val order: Order) : LatestOrdersEvents()
}