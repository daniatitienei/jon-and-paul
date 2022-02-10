package com.jonandpaul.jonandpaul.ui.screens.cart

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jonandpaul.jonandpaul.domain.model.Product
import com.jonandpaul.jonandpaul.domain.model.toProduct
import com.jonandpaul.jonandpaul.domain.use_case.address_datastore.ShippingDetailsUseCases
import com.jonandpaul.jonandpaul.domain.use_case.firestore.FirestoreUseCases
import com.jonandpaul.jonandpaul.ui.utils.Resource
import com.jonandpaul.jonandpaul.ui.utils.Screens
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import com.jonandpaul.jonandpaul.ui.utils.enums.OrderStatus
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class CartViewModel @Inject constructor(
    shippingDetailsUseCases: ShippingDetailsUseCases,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val moshi: Moshi,
    private val useCases: FirestoreUseCases
) : ViewModel() {

    private var _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    val currentShippingDetails = shippingDetailsUseCases.getShippingDetails()

    private var _subtotal = mutableStateOf<Double>(0.0)
    val subtotal: State<Double> = _subtotal

    private var _total = mutableStateOf(0.0)
    val total = _total

    private var _state = mutableStateOf(CartState())
    val state = _state

    init {
        _state.value = _state.value.copy(
            isLoading = true
        )

        getCartItems()
    }

    fun onEvent(event: CartEvents) {
        when (event) {
            is CartEvents.HideModalBottomSheet -> {
                emitEvent(UiEvent.ModalBottomSheet)
            }
            is CartEvents.ShowModalBottomSheet -> {
                emitEvent(UiEvent.ModalBottomSheet)
            }
            is CartEvents.OnAddressClick -> {
                emitEvent(UiEvent.Navigate(route = Screens.Address.route))
            }
            is CartEvents.OnDeleteProduct -> {
                useCases.cart.deleteCartItem(id = event.id)
                emitEvent(UiEvent.Toast)
            }
            is CartEvents.OnNavigationClick -> {
                emitEvent(UiEvent.PopBackStack)
            }
            is CartEvents.OnOrderClick -> {
                val orderId = Random.nextInt(0, 1000000)
                val orderStatus = OrderStatus.PLACED.ordinal

                val order = hashMapOf(
                    "items" to _state.value.items,
                    "shippingDetails" to event.shippingDetails,
                    "date" to Timestamp.now(),
                    "id" to orderId,
                    "total" to _total.value,
                    "status" to orderStatus
                )

                firestore.collection("orders")
                    .add(order)
                    .addOnSuccessListener {
                        firestore.collection("users/${auth.currentUser!!.uid}/orders")
                            .add(order)

                        useCases.cart.clearCart()

                        emitEvent(UiEvent.Navigate(route = Screens.OrderPlaced.route))
                    }
            }
            is CartEvents.OnUpdateQuantity -> {
                useCases.cart.updateQuantity(id = event.id, quantity = event.quantity)
            }
            is CartEvents.OnProductClick -> {
                val jsonAdapter = moshi.adapter(Product::class.java)

                val cartItemWithImageUrlFormatted = event.item.copy(
                    imageUrl = URLEncoder.encode(
                        event.item.imageUrl,
                        StandardCharsets.UTF_8.toString()
                    )
                )
                val productJson =
                    jsonAdapter.toJson(cartItemWithImageUrlFormatted.toProduct())

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

    private fun getCartItems() {
        useCases.cart.getCartItems().onEach { response ->
            when (response) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        items = response.data!!,
                        isLoading = false
                    )

                    calculateTotal()
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        error = response.error,
                        isLoading = false
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun calculateTotal() {

        var currentSubtotal = 0.0

        _state.value.items.map { item ->
            currentSubtotal += item.price * item.quantity
        }

        _subtotal.value = currentSubtotal
        _total.value = currentSubtotal + 15.0
    }
}