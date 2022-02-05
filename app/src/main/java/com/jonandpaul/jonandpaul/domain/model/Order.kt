package com.jonandpaul.jonandpaul.domain.model

data class Order(
    val items: List<Product> = emptyList(),
    val shippingDetails: ShippingDetails = ShippingDetails(),
    val status: String = "",
    val date: String = "",
    val id: Int = 0,
    val total: Double = 0.0,
)
