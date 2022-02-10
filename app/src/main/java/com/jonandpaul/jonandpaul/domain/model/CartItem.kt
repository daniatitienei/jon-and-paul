package com.jonandpaul.jonandpaul.domain.model

data class CartItem(
    val amount: Int = 0,
    val imageUrl: String = "",
    val modelSizeInfo: String = "",
    val size: String = "",
    val price: Double = 0.0,
    val title: String = "",
    val quantity: Int = 0,
    val composition: String = "",
    val id: Int = 0
)

fun CartItem.toProduct(): Product =
    Product(
        price = price,
        title = title,
        amount = amount,
        composition = composition,
        imageUrl = imageUrl,
        modelSizeInfo = modelSizeInfo,
        size = size,
        id = id
    )