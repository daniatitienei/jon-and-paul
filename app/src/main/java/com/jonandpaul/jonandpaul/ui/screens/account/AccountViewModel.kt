package com.jonandpaul.jonandpaul.ui.screens.account

import android.app.Application
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jonandpaul.jonandpaul.R
import com.jonandpaul.jonandpaul.ui.utils.Screens
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val context: Application
) : ViewModel() {

    private var _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    fun onEvent(event: AccountEvents) {
        when (event) {
            is AccountEvents.OnPopBackStack -> {
                emitEvent(UiEvent.PopBackStack)
            }
            is AccountEvents.OnOrdersClick -> {
                emitEvent(UiEvent.Navigate(route = Screens.LatestOrders.route))
            }
            is AccountEvents.OnAlertDialog -> {
                emitEvent(UiEvent.AlertDialog(isOpen = event.isOpen))
            }
            is AccountEvents.OnSendFeedback -> {
                firestore.collection("feedback")
                    .add(
                        hashMapOf(
                            "message" to event.message,
                            "date" to Timestamp.now()
                        )
                    )

                emitEvent(UiEvent.AlertDialog(isOpen = false))
                emitEvent(UiEvent.Toast(message = context.getString(R.string.feedback_sent)))
            }
        }
    }

    private fun emitEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
}