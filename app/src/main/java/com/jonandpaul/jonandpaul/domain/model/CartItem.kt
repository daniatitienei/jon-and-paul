package com.jonandpaul.jonandpaul.domain.model


data class CartItem(
    val amount: Long = 0,
    val imageUrl: String = "",
    val modelSizeInfo: String = "",
    val size: String = "",
    val price: Double = 0.0,
    val title: String = "",
    val quantity: Long = 0,
    val composition: String = ""
)

fun CartItem.toProduct(): Product =
    Product(
        price = price,
        title = title,
        amount = amount.toInt(),
        composition = composition,
        imageUrl = imageUrl,
        modelSizeInfo = modelSizeInfo,
        size = size
    )