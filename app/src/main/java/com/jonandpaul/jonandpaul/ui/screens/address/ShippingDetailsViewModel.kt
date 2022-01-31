package com.jonandpaul.jonandpaul.ui.screens.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonandpaul.jonandpaul.domain.use_case.address_datastore.ShippingDetailsUseCases
import com.jonandpaul.jonandpaul.domain.use_case.counties_api.GetCounties
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShippingDetailsViewModel @Inject constructor(
    private val shippingDetailsUseCases: ShippingDetailsUseCases,
    private val getCounties: GetCounties
) : ViewModel() {

    val counties = getCounties()

    private var _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    fun onEvent(event: ShippingDetailsEvents) {
        when (event) {
            is ShippingDetailsEvents.OnNavigationClick -> {
                emitEvent(UiEvent.PopBackStack)
            }
            is ShippingDetailsEvents.OnSaveClick -> {
                viewModelScope.launch {
                    shippingDetailsUseCases.saveShippingDetails(shippingDetails = event.shippingDetails)
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