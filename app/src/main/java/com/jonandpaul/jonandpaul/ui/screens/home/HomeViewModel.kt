package com.jonandpaul.jonandpaul.ui.screens.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import com.jonandpaul.jonandpaul.ui.utils.Screens
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private var _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    private var _products = mutableStateOf(HomeState())
    val products: State<HomeState> = _products

    init {
        getProducts()
    }

    fun onEvent(event: HomeEvents) {
        when (event) {
            is HomeEvents.OnAccountClick -> {
                emitEvent(UiEvent.Navigate(route = ""))
            }
            is HomeEvents.OnBagClick -> {
                emitEvent(UiEvent.Navigate(route = ""))
            }
            is HomeEvents.OnFavoritesClick -> {
                emitEvent(UiEvent.Navigate(route = ""))
            }
            is HomeEvents.OnProductClick -> {
                emitEvent(UiEvent.Navigate(route = Screens.InspectProduct.route))
            }
            is HomeEvents.OnSearchClick -> {
                emitEvent(UiEvent.BackDropScaffold(isOpen = true))
            }
        }
    }

    private fun emitEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(uiEvent)
        }
    }

    private fun getProducts() {
        firestore.collection("products")
            .addSnapshotListener { snapshots, error ->
                if (error != null)
                    return@addSnapshotListener

                _products.value = _products.value.copy(products = snapshots?.toObjects()!!, isLoading = false)

                Log.d("product_state", _products.toString())
            }
    }
}