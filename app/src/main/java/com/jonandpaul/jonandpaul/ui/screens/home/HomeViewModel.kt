package com.jonandpaul.jonandpaul.ui.screens.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.jonandpaul.jonandpaul.domain.model.CartProduct
import com.jonandpaul.jonandpaul.domain.model.Product
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
    private val auth: FirebaseAuth
) : ViewModel() {

    private var _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    private var _homeState = mutableStateOf(HomeState())
    val homeState: State<HomeState> = _homeState

    init {
        getProducts()
    }

    fun onEvent(event: HomeEvents) {
        when (event) {
            is HomeEvents.OnAccountClick -> {
                emitEvent(
                    UiEvent.Navigate(
                        route = if (auth.currentUser == null) Screens.Register.route else Screens.Account.route
                    )
                )
            }
            is HomeEvents.OnCartClick -> {
                if (auth.currentUser != null)
                    getCardProducts()
                else
                    emitEvent(UiEvent.Navigate(route = Screens.Register.route))
            }
            is HomeEvents.OnFavoritesClick -> {

            }
            is HomeEvents.OnProductClick -> {
                val jsonAdapter = moshi.adapter(Product::class.java)

                event.product = event.product.copy(
                    imageUrl = URLEncoder.encode(
                        event.product.imageUrl,
                        StandardCharsets.UTF_8.toString()
                    )
                )
                val productJson = jsonAdapter.toJson(event.product)

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

                _homeState.value =
                    _homeState.value.copy(products = snapshots?.toObjects()!!, isLoading = false)

                Log.d("product_state", _homeState.toString())
            }
    }

    private fun getCardProducts() {
        auth.currentUser?.let { currentUser ->
            firestore.collection("users").document(currentUser.uid)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.w(TAG, "Listen failed", error)
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        val cart = snapshot.toObject<Cart>()
                        _homeState.value =
                            _homeState.value.copy(cartProducts = cart!!.cart)

                        Log.d("cart", _homeState.value.cartProducts.toString())
                    } else {
                        Log.d(TAG, "Cart: null")
                    }
                }
        }
    }
}

private data class Cart(
    val cart: List<CartProduct> = emptyList()
)