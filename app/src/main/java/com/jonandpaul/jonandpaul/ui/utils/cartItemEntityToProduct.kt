package com.jonandpaul.jonandpaul.ui.utils

import com.jonandpaul.jonandpaul.CartItemEntity
import com.jonandpaul.jonandpaul.domain.model.Product

fun CartItemEntity.toProduct() =
    Product(
        title = title,
        size = size,
        modelSizeInfo = modelSizeInfo,
        imageUrl = imageUrl,
        composition = composition,
        amount = amount.toInt(),
        price = price
    )