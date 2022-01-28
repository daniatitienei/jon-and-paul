package com.jonandpaul.jonandpaul.ui.screens.address

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonandpaul.jonandpaul.domain.use_case.address_datastore.AddressUseCases
import com.jonandpaul.jonandpaul.domain.use_case.counties_api.GetCounties
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val addressUseCases: AddressUseCases,
    private val getCounties: GetCounties
) : ViewModel() {

    val currentAddress = addressUseCases.getAddress()
    val counties = getCounties()

    private var _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    fun onEvent(event: AddressEvents) {
        when (event) {
            is AddressEvents.OnNavigationClick -> {
                emitEvent(UiEvent.PopBackStack)
            }
            is AddressEvents.OnSaveClick -> {
                viewModelScope.launch {
                    addressUseCases.saveAddress(newAddress = event.newAddress)
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