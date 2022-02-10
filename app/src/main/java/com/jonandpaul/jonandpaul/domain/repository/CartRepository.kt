package com.jonandpaul.jonandpaul.domain.repository

import com.jonandpaul.jonandpaul.domain.model.CartItem
import com.jonandpaul.jonandpaul.ui.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    fun getCartItems(): Flow<Resource<List<CartItem>>>

    fun insertCartItem(item: CartItem)

    fun deleteCartItem(id: Int)

    fun updateQuantity(id: Int, quantity: Int)

    fun clearCart()
}