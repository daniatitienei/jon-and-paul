package com.jonandpaul.jonandpaul.ui.screens.create_card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonandpaul.jonandpaul.domain.repository.CreditCardDataSource
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateCreditCardViewModel @Inject constructor(
    private val repository: CreditCardDataSource
) : ViewModel() {

    private var _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    fun onEvent(event: CreateCreditCardEvents) {
        when (event) {
            is CreateCreditCardEvents.OnNavigationClick -> {
                emitEvent(UiEvent.PopBackStack)
            }
            is CreateCreditCardEvents.OnAddCard -> {
                viewModelScope.launch {
                    repository.insertCreditCard(
                        ownerName = event.owner,
                        cvv = event.cvv,
                        number = event.number,
                        expirationDate = event.expirationDate
                    )
                }
                emitEvent(UiEvent.Toast)
                emitEvent(UiEvent.PopBackStack)
            }
        }
    }

    private fun emitEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
}