package com.jonandpaul.jonandpaul.ui.screens.cart

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonandpaul.jonandpaul.domain.repository.CartDataSource
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
    private val repository: CartDataSource
) : ViewModel() {

    private var _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    val cartItems = repository.getCartItems()

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
            is CartEvents.OnCreditCardClick -> {

            }
            is CartEvents.OnDeleteProduct -> {
                viewModelScope.launch {
                    repository.removeItem(id = event.id)
                }
                emitEvent(UiEvent.Toast)
            }
            is CartEvents.OnNavigationClick -> {
                emitEvent(UiEvent.PopBackStack)
            }
            is CartEvents.OnOrderClick -> {

            }
            is CartEvents.OnUpdateQuantity -> {
                viewModelScope.launch {
                    repository.updateQuantityForAnItem(id = event.id, quantity = event.quantity)
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