package com.jonandpaul.jonandpaul.data.repository

import com.jonandpaul.jonandpaul.CreditCardDatabase
import com.jonandpaul.jonandpaul.CreditCardEntity
import com.jonandpaul.jonandpaul.domain.repository.CreditCardDataSource
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow

class CreditCardDataSourceImpl(
    db: CreditCardDatabase
) : CreditCardDataSource {

    private val queries = db.creditCardEntityQueries

    override fun getCreditCards(): Flow<List<CreditCardEntity>> =
        queries.getCreditCards().asFlow().mapToList()

    override suspend fun getCreditCard(id: Long): CreditCardEntity? =
        queries.getCreditCard(id = id).executeAsOneOrNull()

    override suspend fun insertCreditCard(
        ownerName: String,
        number: String,
        cvv: String,
        expirationDate: String,
    ) {
        queries.insertCreditCard(
            id = null,
            ownerName = ownerName,
            number = number,
            cvv = cvv,
            expirationDate = expirationDate,
        )
    }

    override suspend fun deleteCreditCard(id: Long) {
        queries.deleteCreditCard(id = id)
    }

    override suspend fun updateCard(
        id: Long,
        ownerName: String,
        number: String,
        cvv: String,
        expirationDate: String
    ) {
        queries.updateCard(
            id = id,
            ownerName = ownerName,
            number = number,
            cvv = cvv,
            expirationDate = expirationDate,
        )
    }
}