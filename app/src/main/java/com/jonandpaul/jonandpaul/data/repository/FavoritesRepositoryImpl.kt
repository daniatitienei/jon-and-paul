package com.jonandpaul.jonandpaul.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.jonandpaul.jonandpaul.domain.model.Product
import com.jonandpaul.jonandpaul.ui.screens.favorites.FavoritesState
import com.jonandpaul.jonandpaul.domain.repository.FavoritesRepository
import com.jonandpaul.jonandpaul.ui.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class FavoritesRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : FavoritesRepository {
    override fun getFavorites(): Flow<Resource<List<Product>>> = callbackFlow {
        if (auth.currentUser != null) {
            val snapshotListener = firestore.collection("users").document(auth.currentUser!!.uid)
                .addSnapshotListener { snapshot, e ->
                    val response = if (snapshot != null && snapshot.exists()) {
                        val favorites = snapshot.toObject<FavoritesState>()!!.favorites
                        Resource.Success<List<Product>>(data = favorites)
                    } else {
                        Resource.Error<List<Product>>(message = e?.message.toString())
                    }

                    trySend(response).isSuccess
                }
            awaitClose {
                snapshotListener.remove()
            }
        } else {
            trySend(Resource.Loading<List<Product>>())
        }
    }

    override fun removeFavorite(product: Product) {
        if (auth.currentUser != null)
            firestore.collection("users").document(auth.currentUser!!.uid)
                .update("favorites", FieldValue.arrayRemove(product.copy(isFavorite = false)))
    }
}