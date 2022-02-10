package com.jonandpaul.jonandpaul.ui.screens.inspect_product

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonandpaul.jonandpaul.R
import com.jonandpaul.jonandpaul.domain.model.Product
import com.jonandpaul.jonandpaul.domain.model.toCartItem
import com.jonandpaul.jonandpaul.domain.use_case.firestore.FirestoreUseCases
import com.jonandpaul.jonandpaul.ui.utils.Resource
import com.jonandpaul.jonandpaul.ui.utils.Screens
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class InspectProductViewModel @Inject constructor(
    moshi: Moshi,
    private val useCases: FirestoreUseCases,
    private val savedStateHandle: SavedStateHandle,
    private val context: Application
) : ViewModel() {

    private val jsonProductAdapter = moshi.adapter(Product::class.java).lenient()

    private var _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    private var _state = mutableStateOf(InspectProductState())
    val state: State<InspectProductState> = _state

    fun init() {
        val productJson = savedStateHandle.get<String>("product")
        val productObject = jsonProductAdapter.fromJson(productJson!!)

        Log.d("product", productObject?.id.toString())

        _state.value = _state.value.copy(
            product = productObject!!,
            isLoading = true
        )

        getSuggestions()
    }

    fun onEvent(event: InspectProductEvents) {
        when (event) {
            is InspectProductEvents.OnPopBackStack -> {
                emitEvent(event = UiEvent.PopBackStack)
            }
            is InspectProductEvents.OnProductClick -> {

                event.product = event.product.copy(
                    imageUrl = URLEncoder.encode(
                        event.product.imageUrl,
                        StandardCharsets.UTF_8.toString()
                    )
                )

                val productJson = jsonProductAdapter.toJson(event.product)

                emitEvent(
                    event = UiEvent.Navigate(
                        route = Screens.InspectProduct.route.replace(
                            "{product}",
                            productJson
                        )
                    )
                )
            }
            is InspectProductEvents.OnAddToCartClick -> {
                useCases.cart.insertCartItem(event.product.toCartItem())
                emitEvent(UiEvent.Toast(message = context.getString(R.string.item_added_to_cart)))
            }
            is InspectProductEvents.OnFavoriteClick -> {
                if (event.product.isFavorite)
                    useCases.favorites.deleteFavorite(product = event.product.copy(isFavorite = true))
                else
                    useCases.favorites.insertFavorite(product = event.product.copy(isFavorite = true))
            }
        }
    }

    private fun emitEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    private fun getSuggestions() {
        useCases.getSuggestions(product = _state.value.product).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        suggestions = result.data!!,
                    )

                    getFavorites()
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        error = result.error
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getFavorites() {
        useCases.favorites.getFavorites().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        favorites = result.data!!
                    )

                    if (_state.value.favorites.contains(_state.value.product))
                        _state.value = _state.value.copy(
                            product = _state.value.product.copy(isFavorite = true)
                        )

                    if (_state.value.favorites.isNotEmpty()) {
                        _state.value.suggestions.forEach { product ->
                            val isFavorite = _state.value.favorites.contains(product)

                            if (isFavorite)
                                _state.value = _state.value.copy(
                                    suggestions = _state.value.suggestions.map {
                                        if (it == product) it.copy(isFavorite = true)
                                        else it
                                    }
                                )
                        }
                    } else _state.value = _state.value.copy(
                        suggestions = _state.value.suggestions.map {
                            it.copy(isFavorite = false)
                        },
                    )

                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(error = result.error)

                    Log.d("result_error", result.error.toString())
                }
            }
        }.launchIn(viewModelScope)
    }
}