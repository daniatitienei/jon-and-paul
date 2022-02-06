package com.jonandpaul.jonandpaul.ui.screens.favorites

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.jonandpaul.jonandpaul.domain.model.Product
import com.jonandpaul.jonandpaul.domain.model.User
import com.jonandpaul.jonandpaul.ui.utils.Screens
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val moshi: Moshi
) : ViewModel() {

    private var _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    private var _state = mutableStateOf<User>(User())
    val state: State<User> = _state

    private val docRef = firestore.collection("users").document(auth.currentUser!!.uid)

    init {
        getFavorites()
    }

    fun onEvent(event: FavoritesEvents) {
        when (event) {
            is FavoritesEvents.OnFavoriteClick -> {
                removeFavorite(product = event.product)
            }
            is FavoritesEvents.OnNavigationClick -> {
                emitEvent(UiEvent.PopBackStack)
            }
            is FavoritesEvents.OnProductClick -> {
                val jsonAdapter = moshi.adapter(Product::class.java)

                val product = event.product.copy(
                    imageUrl = URLEncoder.encode(
                        event.product.imageUrl,
                        StandardCharsets.UTF_8.toString()
                    )
                )
                val productJson = jsonAdapter.toJson(product)

                emitEvent(
                    UiEvent.Navigate(
                        route = Screens.InspectProduct.route.replace("{product}", productJson)
                    )
                )
            }
        }
    }

    private fun emitEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }


    private fun getFavorites() {
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null)
                return@addSnapshotListener

            if (snapshot != null && snapshot.exists())
                _state.value = snapshot.toObject<User>()!!
        }
    }

    private fun removeFavorite(product: Product) {
        firestore.collection("users").document(auth.currentUser!!.uid)
            .update("favorites", FieldValue.arrayRemove(product.copy(isFavorite = false)))
    }
}