package com.jonandpaul.jonandpaul.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.jonandpaul.jonandpaul.domain.model.Order
import com.jonandpaul.jonandpaul.domain.model.OrderDao
import com.jonandpaul.jonandpaul.domain.model.toOrder
import com.jonandpaul.jonandpaul.domain.repository.OrderRepository
import com.jonandpaul.jonandpaul.ui.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class OrderRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : OrderRepository {

    override fun getOrderById(id: Int): Flow<Resource<Order>> = callbackFlow {
        val listener = firestore.collection("users/${auth.currentUser!!.uid}/orders")
            .whereEqualTo("id", id)
            .addSnapshotListener { snapshot, error ->
                val response = if (error == null && snapshot?.documents?.isNotEmpty() == true) {
                    val orderDao = snapshot.documents[0].toObject<OrderDao>()
                    val order = orderDao!!.toOrder()

                    Resource.Success<Order>(data = order)
                } else {
                    Resource.Error<Order>(message = error?.message.toString())
                }

                trySend(response).isSuccess
            }

        awaitClose {
            listener.remove()
        }
    }

    override fun getOrders(): Flow<Resource<List<Order>>> = callbackFlow {
        val listener = firestore.collection("users/${auth.currentUser!!.uid}/orders")
            .addSnapshotListener { snapshot, error ->
                val response = if (error == null && snapshot?.documents?.isNotEmpty() == true) {
                    Resource.Success<List<Order>>(
                        data = snapshot.toObjects<OrderDao>().map { it.toOrder() })
                } else {
                    Resource.Error<List<Order>>(message = error?.message.toString())
                }

                trySend(response).isSuccess
            }

        awaitClose {
            listener.remove()
        }
    }
}