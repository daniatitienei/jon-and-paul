package com.jonandpaul.jonandpaul.ui.screens.inspect_product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class InspectProductViewModel : ViewModel() {

    private var _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    fun onEvent(event: InspectProductEvents) {
        when (event) {
            is InspectProductEvents.OnPopBackStack -> {
                emitEvent(event = UiEvent.PopBackStack)
            }
        }
    }

    private fun emitEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
}