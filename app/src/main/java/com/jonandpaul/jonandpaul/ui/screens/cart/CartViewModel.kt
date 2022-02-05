package com.jonandpaul.jonandpaul.ui.screens.cart

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.jonandpaul.jonandpaul.domain.repository.CartDataSource
import com.jonandpaul.jonandpaul.domain.use_case.address_datastore.ShippingDetailsUseCases
import com.jonandpaul.jonandpaul.ui.utils.Screens
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartDataSource,
    private val shippingDetailsUseCases: ShippingDetailsUseCases,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private var _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    val cartItems = cartRepository.getCartItems()
    val currentShippingDetails = shippingDetailsUseCases.getShippingDetails()

    private var _subtotal = mutableStateOf<Double>(0.0)
    val subtotal: State<Double> = _subtotal

    private var _total = mutableStateOf(0.0)
    val total = _total

    init {
        calculateTotal()
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
                viewModelScope.launch {
                    cartRepository.removeItem(id = event.id)
                }
                emitEvent(UiEvent.Toast)
            }
            is CartEvents.OnNavigationClick -> {
                emitEvent(UiEvent.PopBackStack)
            }
            is CartEvents.OnOrderClick -> {
                firestore.collection("orders")
                    .add(
                        hashMapOf(
                            "items" to event.items,
                            "shippingDetails" to event.shippingDetails
                        )
                    )
                    .addOnSuccessListener {
                        firestore.collection("users/${auth.currentUser!!.uid}/orders")
                            .add(
                                hashMapOf(
                                    "items" to event.items,
                                    "shippingDetails" to event.shippingDetails
                                )
                            )

                        viewModelScope.launch {
                            cartRepository.clearCart()
                        }
                        emitEvent(UiEvent.Navigate(route = Screens.OrderPlaced.route))
                    }
            }
            is CartEvents.OnUpdateQuantity -> {
                viewModelScope.launch {
                    cartRepository.updateQuantityForAnItem(id = event.id, quantity = event.quantity)
                }
            }
        }
    }

    private fun emitEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    private fun calculateTotal() {

        viewModelScope.launch {
            cartItems.collect { items ->

                var currentSubtotal = 0.0

                items.map { item ->
                    currentSubtotal += item.price * item.quantity
                }

                _subtotal.value = currentSubtotal
                _total.value = currentSubtotal + 15.0
            }
        }
    }
}