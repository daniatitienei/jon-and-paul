package com.jonandpaul.jonandpaul.domain.use_case.firestore.orders

import com.jonandpaul.jonandpaul.domain.model.Order
import com.jonandpaul.jonandpaul.domain.repository.OrderRepository
import com.jonandpaul.jonandpaul.ui.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOrderById @Inject constructor(
    private val repository: OrderRepository
) {
    operator fun invoke(id: Int): Flow<Resource<Order>> = repository.getOrderById(id = id)
}