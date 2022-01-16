package com.jonandpaul.jonandpaul.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private var _uiState = MutableSharedFlow<UiEvent>()
    val uiState: SharedFlow<UiEvent> = _uiState.asSharedFlow()

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
                emitEvent(UiEvent.Navigate(route = ""))
            }
            is HomeEvents.OnSearchClick -> {
                emitEvent(UiEvent.BackDropScaffold(isOpen = true))
            }
        }
    }

    private fun emitEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            _uiState.emit(uiEvent)
        }
    }
}