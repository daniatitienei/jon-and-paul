package com.jonandpaul.jonandpaul.ui.screens.inspect_order

import com.jonandpaul.jonandpaul.domain.model.Order

data class InspectOrderState(
    val order: Order = Order(),
    val isLoading: Boolean = false,
    val error: String? = null
)