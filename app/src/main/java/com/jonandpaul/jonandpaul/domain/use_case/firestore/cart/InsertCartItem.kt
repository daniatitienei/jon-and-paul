package com.jonandpaul.jonandpaul.domain.use_case.firestore.cart

import com.jonandpaul.jonandpaul.domain.model.CartItem
import com.jonandpaul.jonandpaul.domain.repository.CartRepository
import javax.inject.Inject

class InsertCartItem @Inject constructor(
    private val repository: CartRepository
) {
    operator fun invoke(item: CartItem) = repository.insertCartItem(item)
}