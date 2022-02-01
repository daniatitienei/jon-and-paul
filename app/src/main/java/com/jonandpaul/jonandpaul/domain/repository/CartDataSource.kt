package com.jonandpaul.jonandpaul.domain.repository

import com.jonandpaul.jonandpaul.CartItemEntity
import kotlinx.coroutines.flow.Flow

interface CartDataSource {
    fun getCartItems(): Flow<List<CartItemEntity>>

    suspend fun addToCart(
        id: Long?,
        title: String,
        amount: Long,
        quantity: Long,
        composition: String,
        size: String,
        price: Double,
        imageUrl: String,
        modelSizeInfo: String
    )

    suspend fun removeItem(id: Long)

    suspend fun updateQuantityForAnItem(id: Long, quantity: Long)

    suspend fun clearCart()
}