package com.jonandpaul.jonandpaul.domain.use_case.firestore.cart

import com.jonandpaul.jonandpaul.domain.repository.CartRepository
import javax.inject.Inject

class DeleteCartItem @Inject constructor(
    private val repository: CartRepository
) {
    operator fun invoke(id: Int) = repository.deleteCartItem(id)
}