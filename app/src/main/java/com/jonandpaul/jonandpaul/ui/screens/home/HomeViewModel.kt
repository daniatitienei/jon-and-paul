package com.jonandpaul.jonandpaul.ui.screens.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import com.jonandpaul.jonandpaul.domain.model.Product
import com.jonandpaul.jonandpaul.domain.repository.CartDataSource
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
class HomeViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val moshi: Moshi,
    private val auth: FirebaseAuth,
    private val cartRepository: CartDataSource,
    private val useCases: FavoritesUseCases
) : ViewModel() {

    private var _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    private var _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    val cartItems = cartRepository.getCartItems()

    init {
        getProducts()
    }

    fun onEvent(event: HomeEvents) {
        when (event) {
            is HomeEvents.OnAccountClick -> {
                emitEvent(
                    UiEvent.Navigate(
                        route = Screens.Account.route
                    )
                )
            }
            is HomeEvents.OnCartClick -> {
                emitEvent(UiEvent.Navigate(route = Screens.Cart.route))
            }
            is HomeEvents.OnFavoritesClick -> {
                emitEvent(UiEvent.Navigate(route = Screens.Favorites.route))
            }
            is HomeEvents.OnProductClick -> {
                val jsonAdapter = moshi.adapter(Product::class.java)

                val productWithImageUrlFormatted = event.product.copy(
                    imageUrl = URLEncoder.encode(
                        event.product.imageUrl,
                        StandardCharsets.UTF_8.toString()
                    )
                )
                val productJson = jsonAdapter.toJson(productWithImageUrlFormatted)

                emitEvent(
                    UiEvent.Navigate(
                        route = Screens.InspectProduct.route.replace("{product}", productJson)
                    )
                )
            }
            is HomeEvents.ShowModalBottomSheet -> {
                emitEvent(UiEvent.ModalBottomSheet)
            }
            is HomeEvents.HideModalBottomSheet -> {
                emitEvent(UiEvent.ModalBottomSheet)
            }
            is HomeEvents.RevealBackdrop -> {
                emitEvent(UiEvent.BackdropScaffold)
            }
            is HomeEvents.ConcealBackdrop -> {
                emitEvent(UiEvent.BackdropScaffold)
            }
            is HomeEvents.OnFavoriteClick -> {
                if (event.isFavorite)
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

    private fun getProducts() {
        Log.d("user_products", auth.currentUser?.uid.toString())

        firestore.collection("products")
            .addSnapshotListener { snapshots, error ->
                if (error != null)
                    return@addSnapshotListener

                _state.value =
                    _state.value.copy(products = snapshots?.toObjects()!!, isLoading = true)

                getFavorites()
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

    private fun getFavorites() {
        useCases.getFavorites().onEach { result ->

            Log.d("result", result.toString())

            when (result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        favorites = result.data!!
                    )

                    if (_state.value.favorites.isNotEmpty()) {
                        _state.value.products.forEach { product ->
                            val isFavorite = _state.value.favorites.contains(product)

                            if (isFavorite)
                                _state.value = _state.value.copy(
                                    products = _state.value.products.map {
                                        if (it == product) it.copy(isFavorite = true)
                                        else it
                                    }
                                )
                        }
                    } else _state.value = _state.value.copy(
                        products = _state.value.products.map {
                            it.copy(isFavorite = false)
                        },
                    )

                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(error = result.error)

                    Log.d("result_error", result.error.toString())
                }
            }
        }.launchIn(viewModelScope)
    }
}