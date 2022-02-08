package com.jonandpaul.jonandpaul.domain.model

import com.google.firebase.Timestamp
import com.jonandpaul.jonandpaul.ui.utils.enums.OrderStatus

data class Order(
    val items: List<CartItem> = emptyList(),
    val shippingDetails: ShippingDetails = ShippingDetails(),
    val status: OrderStatus = OrderStatus.PLACED,
    val date: Timestamp = Timestamp.now(),
    val id: Int = 0,
    val total: Double = 0.0,
)
