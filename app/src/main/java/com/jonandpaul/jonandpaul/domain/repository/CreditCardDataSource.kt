package com.jonandpaul.jonandpaul.domain.repository

import com.jonandpaul.jonandpaul.CreditCardEntity
import kotlinx.coroutines.flow.Flow

interface CreditCardDataSource {
    fun getCreditCards(): Flow<List<CreditCardEntity>>

    suspend fun getCreditCard(id: Long): CreditCardEntity?

    suspend fun insertCreditCard(
        ownerName: String,
        number: String,
        cvv: String,
        expirationDate: String,
    )

    suspend fun deleteCreditCard(id: Long)

    suspend fun updateCard(
        id: Long,
        ownerName: String,
        number: String,
        cvv: String,
        expirationDate: String,
    )
}