package com.jonandpaul.jonandpaul.ui.screens.latest_orders

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import com.jonandpaul.jonandpaul.domain.model.Order
import com.jonandpaul.jonandpaul.domain.model.OrderDao
import com.jonandpaul.jonandpaul.domain.model.toOrder
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
class LatestOrdersViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val useCases: FirestoreUseCases
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private var _state = mutableStateOf(LatestOrdersState())
    val state: State<LatestOrdersState> = _state

    init {
        getLatestOrders()
    }

    fun onEvent(event: LatestOrdersEvents) {
        when (event) {
            is LatestOrdersEvents.OnNavigationIconClick -> {
                emitEvent(UiEvent.PopBackStack)
            }
            is LatestOrdersEvents.OnOrderClick -> {

                emitEvent(
                    UiEvent.Navigate(
                        route = Screens.InspectOrder.route.replace(
                            "{orderId}", event.order.id.toString()
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

    private fun getLatestOrders() {
        useCases.orders.getOrders().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value =
                        _state.value.copy(latestOrders = result.data!!, isLoading = false)
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