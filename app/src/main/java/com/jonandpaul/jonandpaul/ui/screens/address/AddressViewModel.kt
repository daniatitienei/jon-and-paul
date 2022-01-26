package com.jonandpaul.jonandpaul.ui.screens.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.jonandpaul.jonandpaul.domain.use_case.address_datastore.AddressUseCases
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val useCases: AddressUseCases
) : ViewModel() {

    val currentAddress = useCases.getAddress()

    private var _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    fun onEvent(event: AddressEvents) {
        when (event) {
            is AddressEvents.OnNavigationClick -> {
                emitEvent(UiEvent.PopBackStack)
            }
            is AddressEvents.OnSaveClick -> {
                viewModelScope.launch {
                    useCases.saveAddress(newAddress = event.newAddress)
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