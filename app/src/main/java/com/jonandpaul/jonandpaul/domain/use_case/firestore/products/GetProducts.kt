package com.jonandpaul.jonandpaul.domain.use_case.firestore.products

import com.jonandpaul.jonandpaul.domain.model.Product
import com.jonandpaul.jonandpaul.domain.repository.ProductsRepository
import com.jonandpaul.jonandpaul.ui.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProducts @Inject constructor(
    private val repository: ProductsRepository
) {
    operator fun invoke(): Flow<Resource<List<Product>>> = repository.getProducts()
}