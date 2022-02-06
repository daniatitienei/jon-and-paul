package com.jonandpaul.jonandpaul.ui.screens.inspect_order

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.jonandpaul.jonandpaul.domain.model.Product
import com.jonandpaul.jonandpaul.domain.model.toProduct
import com.jonandpaul.jonandpaul.ui.utils.Screens
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class InspectOrderViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val moshi: Moshi
) : ViewModel() {

    private val _state = mutableStateOf(InspectOrderState())
    val state: State<InspectOrderState> = _state

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        getOrderInfo()
    }

    fun onEvent(event: InspectOrderEvents) {
        when (event) {
            is InspectOrderEvents.OnNavigationIconClick -> {
                emitEvent(UiEvent.PopBackStack)
            }
            is InspectOrderEvents.OnProductClick -> {
                val jsonAdapter = moshi.adapter(Product::class.java)

                val productWithImageUrlFormatted = event.item.copy(
                    imageUrl = URLEncoder.encode(
                        event.item.imageUrl,
                        StandardCharsets.UTF_8.toString()
                    )
                )

                val productJson = jsonAdapter.toJson(productWithImageUrlFormatted.toProduct())

                emitEvent(
                    UiEvent.Navigate(
                        route = Screens.InspectProduct.route.replace(
                            "{product}",
                            productJson
                        )
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

    private fun getOrderInfo() {
        _state.value = _state.value.copy(isLoading = true)

        val orderId = savedStateHandle.get<String>("orderId")!!.toInt()

        Log.d("order", orderId.toString())

        firestore.collection("users/${auth.currentUser!!.uid}/orders")
            .whereEqualTo("id", orderId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents)
                    _state.value = _state.value.copy(
                        order = document.toObject(),
                        isLoading = false
                    )

                Log.d("order", _state.value.order.items.toString())
            }
    }
}