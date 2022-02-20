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
import com.google.firebase.firestore.ktx.toObjects
import com.jonandpaul.jonandpaul.domain.model.OrderDao
import com.jonandpaul.jonandpaul.domain.model.Product
import com.jonandpaul.jonandpaul.domain.model.toOrder
import com.jonandpaul.jonandpaul.domain.model.toProduct
import com.jonandpaul.jonandpaul.domain.use_case.firestore.FirestoreUseCases
import com.jonandpaul.jonandpaul.ui.utils.Resource
import com.jonandpaul.jonandpaul.ui.utils.Screens
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class InspectOrderViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val moshi: Moshi,
    private val useCases: FirestoreUseCases
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
        val orderId = savedStateHandle.get<String>("orderId")!!.toInt()

        useCases.orders.getOrderById(orderId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value =
                        _state.value.copy(order = result.data!!, isLoading = false)
                }
                is Resource.Loading -> {
                    _state.value =
                        _state.value.copy(isLoading = true)
                }
                is Resource.Error -> {
                    _state.value =
                        _state.value.copy(error = result.error, isLoading = false)
                }
            }
        }.launchIn(viewModelScope)
    }
}