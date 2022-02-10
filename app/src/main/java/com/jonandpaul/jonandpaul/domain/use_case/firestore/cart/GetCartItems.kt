package com.jonandpaul.jonandpaul.domain.use_case.firestore.cart

import com.jonandpaul.jonandpaul.domain.model.CartItem
import com.jonandpaul.jonandpaul.domain.repository.CartRepository
import com.jonandpaul.jonandpaul.ui.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartItems @Inject constructor(
    private val repository: CartRepository
) {
    operator fun invoke(): Flow<Resource<List<CartItem>>> = repository.getCartItems()
}