package com.jonandpaul.jonandpaul.ui.screens.account

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
class AccountViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private var _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    fun onEvent(event: AccountEvents) {
        when (event) {
            is AccountEvents.OnPopBackStack -> {
                emitEvent(UiEvent.PopBackStack)
            }
            is AccountEvents.OnOrdersClick -> {
                /*TODO*/
            }
            is AccountEvents.OnCreditCardClick -> {
                /*TODO*/
            }
            is AccountEvents.OnInfoClick -> {
                /*TODO*/
            }
            is AccountEvents.OnLogoutClick -> {
                auth.signOut()
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