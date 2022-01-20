package com.jonandpaul.jonandpaul.ui.screens.register

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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
class RegisterViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private var _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    private var _emailError = mutableStateOf<String?>(null)
    val emailError: State<String?> = _emailError

    private var _passwordError = mutableStateOf<String?>(null)
    val passwordError: State<String?> = _passwordError

    fun onEvent(event: RegisterEvents) {
        when (event) {
            is RegisterEvents.OnRegisterClick -> {
                registerWithEmailAndPassword(email = event.email, password = event.password)
            }
            is RegisterEvents.OnAlreadyHaveAnAccount -> {

            }
            is RegisterEvents.OnGoogleClick -> {

            }
        }
    }

    private fun emitEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    private fun registerWithEmailAndPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    Log.d("register_status", "SUCCESS")
                else
                    Log.d("register_status", "FAILED")
            }
    }
}