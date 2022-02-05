package com.jonandpaul.jonandpaul.ui.screens.home

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
class HomeViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val moshi: Moshi,
    private val auth: FirebaseAuth,
    private val cartRepository: CartDataSource
) : ViewModel() {

    private var _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    private var _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    val cartItems = cartRepository.getCartItems()

    private var _user = mutableStateOf(User())

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
                Log.d("isFavorite", event.isFavorite.toString())
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
        firestore.collection("products")
            .addSnapshotListener { snapshots, error ->
                if (error != null)
                    return@addSnapshotListener

                _state.value =
                    _state.value.copy(products = snapshots?.toObjects()!!, isLoading = true)

                getFavorites()

                Log.d("product_state", _state.toString())
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
        firestore.collection("users").document(auth.currentUser!!.uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w("getFavorites", error.message.toString())
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    _user.value = snapshot.toObject<User>()!!

                    val productListWithFavorites = mutableSetOf<Product>()

                    if (_user.value.favorites.isNotEmpty()) {
                        _user.value.favorites.forEach { favorite ->
                            _state.value.products.forEach { product ->
                                if (product.id == favorite.id) {
                                    productListWithFavorites.contains(product)
                                    productListWithFavorites.remove(product)
                                    productListWithFavorites.add(product.copy(isFavorite = true))
                                } else
                                    productListWithFavorites.add(product)
                            }
                        }

                        _state.value = _state.value.copy(
                            products = productListWithFavorites.toList(),
                            isLoading = false
                        )
                    } else _state.value = _state.value.copy(
                        products = _state.value.products.map {
                            it.copy(isFavorite = false)
                        },
                        isLoading = false
                    )

                    Log.d("product", _state.value.products.size.toString())

                    _state.value.products.forEachIndexed { index, product ->
                        Log.d("product at $index", product.isFavorite.toString())
                    }

                    Log.d("favorites", _user.value.toString())
                }
            }
    }
}