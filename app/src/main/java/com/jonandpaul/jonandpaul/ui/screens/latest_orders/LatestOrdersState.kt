package com.jonandpaul.jonandpaul.ui.screens.latest_orders

import com.jonandpaul.jonandpaul.domain.model.Order

data class LatestOrdersState(
    val latestOrders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
