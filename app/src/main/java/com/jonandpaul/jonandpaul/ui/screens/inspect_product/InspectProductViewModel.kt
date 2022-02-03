package com.jonandpaul.jonandpaul.ui.screens.inspect_product

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.jonandpaul.jonandpaul.domain.model.Product
import com.jonandpaul.jonandpaul.domain.model.User
import com.jonandpaul.jonandpaul.domain.repository.CartDataSource
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
class InspectProductViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val moshi: Moshi,
    private val repository: CartDataSource,
    private val auth: FirebaseAuth
) : ViewModel() {

    private var _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    private var _state = mutableStateOf(InspectProductState())
    val state: State<InspectProductState> = _state

    init {
        getSuggestions()
    }


    fun onEvent(event: InspectProductEvents) {
        when (event) {
            is InspectProductEvents.OnPopBackStack -> {
                emitEvent(event = UiEvent.PopBackStack)
            }
            is InspectProductEvents.OnProductClick -> {
                val jsonAdapter = moshi.adapter(Product::class.java)

                event.product = event.product.copy(
                    imageUrl = URLEncoder.encode(
                        event.product.imageUrl,
                        StandardCharsets.UTF_8.toString()
                    )
                )
                val productJson = jsonAdapter.toJson(event.product)

                emitEvent(
                    event = UiEvent.Navigate(
                        route = Screens.InspectProduct.route.replace(
                            "{product}",
                            productJson
                        )
                    )
                )
            }
            is InspectProductEvents.OnAddToCartClick -> {
                viewModelScope.launch {
                    repository.addToCart(
                        id = event.product.id,
                        title = event.product.title,
                        amount = event.product.amount.toLong(),
                        modelSizeInfo = event.product.modelSizeInfo,
                        composition = event.product.composition,
                        imageUrl = event.product.imageUrl,
                        price = event.product.price,
                        size = event.product.size,
                        quantity = 1
                    )
                }
                emitEvent(UiEvent.Toast)
            }
            is InspectProductEvents.OnFavoriteClick -> {
                if (event.product.isFavorite)
                    removeFavorite(product = event.product.copy(isFavorite = false))
                else
                    addFavorite(product = event.product.copy(isFavorite = false))
            }
        }
    }

    private fun emitEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    private fun addFavorite(product: Product) {
        firestore.collection("users").document(auth.currentUser!!.uid)
            .update("favorites", FieldValue.arrayUnion(product))
    }

    private fun removeFavorite(product: Product) {
        firestore.collection("users").document(auth.currentUser!!.uid)
            .update("favorites", FieldValue.arrayRemove(product))
    }

    private fun getSuggestions() {
        firestore.collection("products")
            .get()
            .addOnSuccessListener { result ->
                val suggestions: List<Product> = result.toObjects()
                _state.value =
                    _state.value.copy(
                        suggestions = suggestions.shuffled(),
                        isLoading = false
                    )
            }
    }
}