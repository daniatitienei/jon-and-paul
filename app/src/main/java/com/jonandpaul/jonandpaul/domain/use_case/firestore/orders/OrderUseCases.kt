package com.jonandpaul.jonandpaul.domain.use_case.firestore.orders

data class OrderUseCases(
    val getOrderById: GetOrderById,
    val getOrders: GetOrders
)
