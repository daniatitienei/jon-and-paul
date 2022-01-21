package com.jonandpaul.jonandpaul.ui.screens.register

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.jonandpaul.jonandpaul.R
import com.jonandpaul.jonandpaul.ui.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val context: Application
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
                // TODO: 21.01.2022
            }
            is RegisterEvents.OnGoogleClick -> {

            }
            is RegisterEvents.OnNavigationIconClick -> {
                emitEvent(UiEvent.PopBackStack)
            }
        }
    }

    private fun emitEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    private fun registerWithEmailAndPassword(email: String, password: String) {

        _emailError.value = null
        _passwordError.value = null

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                try {
                    if (!task.isSuccessful)
                        throw task.exception!!
                } catch (e: FirebaseAuthWeakPasswordException) {
                    Log.d("auth_error_weak", e.message!!)
                    _passwordError.value = context.getString(R.string.auth_error_weak_password)
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    Log.d("auth_error_credentials", e.message!!)
                    _emailError.value = context.getString(R.string.auth_error_invalid_email)
                } catch (e: FirebaseAuthUserCollisionException) {
                    Log.d("auth_error_collision", e.message!!)
                    TODO()
                }
            }
    }
}