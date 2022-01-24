package com.jonandpaul.jonandpaul.ui.screens.cart

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import com.jonandpaul.jonandpaul.domain.model.CartProduct
import com.jonandpaul.jonandpaul.domain.model.User
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private var _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    private var _state = mutableStateOf(CartState())
    val state: State<CartState> = _state

    private val docRef = firestore.collection("users").document(auth.currentUser!!.uid)

    init {
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

            }
            is CartEvents.OnCreditCardClick -> {

            }
            is CartEvents.OnDeleteProduct -> {
                deleteItem(event.item)
            }
            is CartEvents.OnNavigationClick -> {
                emitEvent(UiEvent.PopBackStack)
            }
            is CartEvents.OnOrderClick -> {

            }
        }
    }

    private fun emitEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    fun updateQuantity(cartItem: CartProduct, quantity: Int): CartProduct {
        _state.value = _state.value.copy(
            cartItems = _state.value.cartItems.map { item ->
                if (item == cartItem)
                    item.copy(quantity = quantity)
                else item
            }
        )

        docRef.set(
            mapOf("cart" to _state.value.cartItems), SetOptions.merge()
        )

        return cartItem.copy(quantity = quantity)
    }

    private fun getCartItems() {
        auth.currentUser?.let { currentUser ->
            docRef.addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.w(ContentValues.TAG, "Listen failed", error)
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        val cart = snapshot.toObject<User>()?.cart

                        cart?.let {
                            _state.value =
                                _state.value.copy(cartItems = it)

                            Log.d("cart", _state.value.cartItems.toString())
                        }
                    } else {
                        Log.d(ContentValues.TAG, "Cart: null")
                    }
                }
        }
    }

    private fun deleteItem(cartItem: CartProduct) {
        docRef.update("cart", FieldValue.arrayRemove(cartItem))
        getCartItems()
    }
}