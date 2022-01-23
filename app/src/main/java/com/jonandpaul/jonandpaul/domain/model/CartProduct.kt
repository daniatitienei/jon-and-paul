package com.jonandpaul.jonandpaul.domain.model

data class CartProduct(
    val amount: Int = 1,
    val composition: String = "",
    val imageUrl: String = "",
    val modelSizeInfo: String = "",
    val size: String = "Universala",
    val price: Double = 20.00,
    val title: String = "",
    val quantity: Int = 1,
)
