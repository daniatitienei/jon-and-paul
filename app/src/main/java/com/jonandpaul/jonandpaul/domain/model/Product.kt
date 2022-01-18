package com.jonandpaul.jonandpaul.domain.model

data class Product(
    val amount: Int = 1,
    val composition: String = "",
    val imageUrl: String = "",
    val modelSizeInfo: String = "",
    val price: Double = 20.00,
    val title: String = ""
)
