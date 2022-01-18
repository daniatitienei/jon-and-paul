package com.jonandpaul.jonandpaul.ui.screens.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
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
    private val moshi: Moshi
) : ViewModel() {

    private var _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    private var _productsState = mutableStateOf(HomeState())
    val productsState: State<HomeState> = _productsState

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

                _productsState.value =
                    _productsState.value.copy(products = snapshots?.toObjects()!!, isLoading = false)

                Log.d("product_state", _productsState.toString())
            }
    }
}