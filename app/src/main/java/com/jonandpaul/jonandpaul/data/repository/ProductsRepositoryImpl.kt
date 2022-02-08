package com.jonandpaul.jonandpaul.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import com.jonandpaul.jonandpaul.domain.model.Product
import com.jonandpaul.jonandpaul.domain.repository.ProductsRepository
import com.jonandpaul.jonandpaul.ui.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ProductsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ProductsRepository {

    override fun getProducts(): Flow<Resource<List<Product>>> = callbackFlow {
        val snapshotListener = firestore.collection("products")
            .addSnapshotListener { snapshots, e ->
                val response = if (e == null) {
                    val products = snapshots!!.toObjects<Product>()
                    Resource.Success<List<Product>>(data = products)
                } else
                    Resource.Error<List<Product>>(message = e.message.toString())

                trySend(response).isSuccess
            }

        awaitClose {
            snapshotListener.remove()
        }
    }
}