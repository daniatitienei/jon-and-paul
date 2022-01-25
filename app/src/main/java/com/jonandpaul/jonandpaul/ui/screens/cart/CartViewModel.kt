package com.jonandpaul.jonandpaul.ui.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonandpaul.jonandpaul.domain.repository.CartDataSource
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: CartDataSource
) : ViewModel() {

    private var _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    val cartItems = repository.getCartItems()

    fun onEvent(event: CartEvents) {
        when (event) {
            is CartEvents.HideModalBottomSheet -> {
                emitEvent(UiEvent.ModalBottomSheet)
            }
            is CartEvents.ShowModalBottomSheet -> {
                emitEvent(UiEvent.ModalBottomSheet)
            }
            is CartEvents.OnAddressClick -> {

            }
            is CartEvents.OnCreditCardClick -> {

            }
            is CartEvents.OnDeleteProduct -> {
                viewModelScope.launch {
                    repository.removeItem(id = event.id)
                }
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
}