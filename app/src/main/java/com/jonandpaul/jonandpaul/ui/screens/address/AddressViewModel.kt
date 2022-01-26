package com.jonandpaul.jonandpaul.ui.screens.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private var _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    fun onEvent(event: AddressEvents) {
        when (event) {
            is AddressEvents.OnNavigationClick -> {
                emitEvent(UiEvent.PopBackStack)
            }
            is AddressEvents.OnSaveClick -> {
                /*TODO*/
            }
        }
    }

    fun emitEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
}