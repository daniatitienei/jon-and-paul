package com.jonandpaul.jonandpaul.domain.model

import com.google.firebase.Timestamp
import com.jonandpaul.jonandpaul.ui.utils.enums.OrderStatus

data class OrderDao(
    val items: List<CartItem> = emptyList(),
    val shippingDetails: ShippingDetails = ShippingDetails(),
    val status: Int = 0,
    val date: Timestamp = Timestamp.now(),
    val id: Int = 0,
    val total: Double = 0.0,
)

fun OrderDao.toOrder() =
    Order(
        id = id,
        items = items,
        shippingDetails = shippingDetails,
        status = when (status) {
            0 -> OrderStatus.PLACED
            1 -> OrderStatus.ON_DELIVERY
            else -> OrderStatus.COMPLETED
        },
        date = date,
        total = total
    )