package com.jonandpaul.jonandpaul.domain.repository

import kotlinx.coroutines.flow.Flow


interface StoreAddress {
    fun getAddress(): Flow<String>

    suspend fun saveAddress(newAddress: String)
}