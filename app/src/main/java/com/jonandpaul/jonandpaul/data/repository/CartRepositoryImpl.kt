package com.jonandpaul.jonandpaul.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObjects
import com.jonandpaul.jonandpaul.domain.model.CartItem
import com.jonandpaul.jonandpaul.domain.repository.CartRepository
import com.jonandpaul.jonandpaul.ui.utils.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : CartRepository {

    override fun getCartItems(): Flow<Resource<List<CartItem>>> = callbackFlow {
        if (auth.currentUser != null) {
            val listener = firestore.collection("users/${auth.currentUser!!.uid}/cart")
                .addSnapshotListener { snapshots, error ->
                    val result = if (error == null) {
                        if (snapshots?.isEmpty == false)
                            Resource.Success<List<CartItem>>(data = snapshots.toObjects())
                        else
                            Resource.Success<List<CartItem>>(data = emptyList())
                    } else Resource.Error<List<CartItem>>(message = error.message.toString())

                    trySend(result).isSuccess
                }

            awaitClose { listener.remove() }
        }
    }

    override fun insertCartItem(item: CartItem) {
        if (auth.currentUser != null)
            firestore.collection("users/${auth.currentUser!!.uid}/cart")
                .document(item.id.toString())
                .set(item, SetOptions.merge())
                .addOnSuccessListener {
                    Log.d("document_set", "SUCCESS")
                }
                .addOnFailureListener {
                    Log.d("document_set", "FAILURE, ${it.message}")
                }
    }

    override fun deleteCartItem(id: Int) {
        if (auth.currentUser != null)
            firestore.collection("users/${auth.currentUser!!.uid}/cart")
                .document(id.toString())
                .delete()
                .addOnSuccessListener {
                    Log.d("document_delete", "SUCCESS")
                }
                .addOnFailureListener {
                    Log.d("document_delete", "FAILURE, ${it.message}")
                }
    }

    override fun updateQuantity(id: Int, quantity: Int) {
        if (auth.currentUser != null)
            firestore.collection("users/${auth.currentUser!!.uid}/cart")
                .document(id.toString())
                .update("quantity", quantity)
                .addOnSuccessListener {
                    Log.d("document_update", "SUCCESS")
                }
                .addOnFailureListener {
                    Log.d("document_update", "FAILURE, ${it.message}")
                }
    }

    override fun clearCart() {
        if (auth.currentUser != null)
            firestore.collection("users/${auth.currentUser!!.uid}/cart")
                .get()
                .addOnSuccessListener {
                    it.documents.forEach { document ->
                        document.reference.delete()
                    }
                }
    }
}