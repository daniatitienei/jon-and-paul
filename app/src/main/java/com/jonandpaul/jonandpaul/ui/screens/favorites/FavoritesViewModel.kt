package com.jonandpaul.jonandpaul.ui.screens.favorites

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.jonandpaul.jonandpaul.domain.model.Product
import com.jonandpaul.jonandpaul.domain.use_case.firestore.favorites.FavoritesUseCases
import com.jonandpaul.jonandpaul.ui.utils.Resource
import com.jonandpaul.jonandpaul.ui.utils.Screens
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val moshi: Moshi,
    private val useCases: FavoritesUseCases
) : ViewModel() {

    private var _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    private var _state = mutableStateOf<FavoritesState>(FavoritesState())
    val state: State<FavoritesState> = _state

    private val docRef = firestore.collection("users").document(auth.currentUser!!.uid)

    init {
        getFavorites()
    }

    private fun getFavorites() {
        useCases.getFavorites().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        favorites = result.data!!
                    )
                }
                else -> Unit
            }
        }.launchIn(viewModelScope)
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


    private fun removeFavorite(product: Product) {
        useCases.deleteFavorite(product = product)
    }
}