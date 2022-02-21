package com.jonandpaul.jonandpaul.domain.repository

import com.jonandpaul.jonandpaul.domain.model.Order
import com.jonandpaul.jonandpaul.ui.utils.Resource
import kotlinx.coroutines.flow.Flow

interface OrderRepository {

    fun getOrderById(id: Int): Flow<Resource<Order>>

    fun getOrders(): Flow<Resource<List<Order>>>
}