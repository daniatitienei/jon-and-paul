package com.jonandpaul.jonandpaul.domain.repository

import com.jonandpaul.jonandpaul.domain.model.CreditCard
import kotlinx.coroutines.flow.Flow

interface StoreCreditCards {
    fun getCreditCards(): Flow<CreditCard>

    suspend fun addCard(creditCard: CreditCard)
}