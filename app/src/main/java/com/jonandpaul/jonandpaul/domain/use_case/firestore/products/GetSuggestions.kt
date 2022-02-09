package com.jonandpaul.jonandpaul.domain.use_case.firestore.products

import com.jonandpaul.jonandpaul.domain.model.Product
import com.jonandpaul.jonandpaul.domain.repository.ProductsRepository
import com.jonandpaul.jonandpaul.ui.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

class GetSuggestions @Inject constructor(
    private val repository: ProductsRepository
) {
    operator fun invoke(product: Product): Flow<Resource<List<Product>>> = flow {
        try {
            repository.getProducts().collect { response ->
                val products = response.data?.filter {
                    it.id != product.id
                }

                emit(
                    Resource.Success<List<Product>>(
                        data = products?.shuffled()?.subList(0, 5) ?: emptyList()
                    )
                )
            }

        } catch (e: IOException) {

        } catch (e: Exception) {

        }
    }
}