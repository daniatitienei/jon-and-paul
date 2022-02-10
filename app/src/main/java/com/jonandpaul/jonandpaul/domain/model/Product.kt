package com.jonandpaul.jonandpaul.domain.model

data class Product(
    val id: Int? = null,
    val amount: Int = 1,
    val composition: String = "",
    val imageUrl: String = "",
    val modelSizeInfo: String = "",
    val size: String = "UN",
    val price: Double = 20.00,
    val title: String = "",
    val isFavorite: Boolean = false
)

fun Product.toCartItem() =
    CartItem(
        id = id!!,
        amount = amount,
        price = price,
        composition = composition,
        imageUrl = imageUrl,
        modelSizeInfo = modelSizeInfo,
        size = size,
        title = title,
        quantity = 1
    )