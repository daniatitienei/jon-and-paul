package com.jonandpaul.jonandpaul.data.repository

import com.jonandpaul.jonandpaul.CartDatabase
import com.jonandpaul.jonandpaul.CartItemEntity
import com.jonandpaul.jonandpaul.domain.repository.CartDataSource
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow

class CartDataSourceImpl(
    db: CartDatabase
) : CartDataSource {

    private val queries = db.cartItemEntityQueries

    override fun getCartItems(): Flow<List<CartItemEntity>> =
        queries.getCartItems().asFlow().mapToList()

    override suspend fun addToCart(
        id: Long?,
        title: String,
        amount: Long,
        quantity: Long,
        composition: String,
        size: String,
        price: Double,
        imageUrl: String,
        modelSizeInfo: String
    ) {
        queries.addToCart(
            id = id,
            title = title,
            amount = amount,
            quantity = quantity,
            size = size,
            price = price,
            imageUrl = imageUrl,
            modelSizeInfo = modelSizeInfo,
            composition = composition
        )
    }

    override suspend fun removeItem(id: Long) {
        queries.removeFromCart(id = id)
    }

    override suspend fun updateQuantityForAnItem(id: Long, quantity: Long) {
        queries.updateQuantity(id = id, quantity = quantity)
    }

    override suspend fun clearCart() {
        queries.clearCart()
    }
}