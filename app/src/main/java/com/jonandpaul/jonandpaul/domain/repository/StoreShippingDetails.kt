package com.jonandpaul.jonandpaul.domain.repository

import com.jonandpaul.jonandpaul.domain.model.ShippingDetails
import kotlinx.coroutines.flow.Flow


interface StoreShippingDetails {
    fun getShippingDetails(): Flow<ShippingDetails?>

    suspend fun saveShippingDetails(shippingDetails: ShippingDetails)
}