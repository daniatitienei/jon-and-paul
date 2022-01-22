package com.jonandpaul.jonandpaul.ui.screens.login

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.GoogleAuthProvider
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
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val context: Application
) : ViewModel() {

    private var _passwordError = mutableStateOf<String?>(null)
    val passwordError: State<String?> = _passwordError

    private var _emailError = mutableStateOf<String?>(null)
    val emailError: State<String?> = _emailError

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    fun onEvent(event: LoginEvents) {
        when (event) {
            is LoginEvents.OnContinueWithGoogle -> {
                continueWithGoogle(idToken = event.idToken)
            }
            is LoginEvents.OnLoginClick -> {
                loginWithEmailAndPassword(email = event.email, password = event.password)
            }
            is LoginEvents.OnPopBackStack -> {
                emitEvent(UiEvent.PopBackStack)
            }
            is LoginEvents.OnRegisterHereClick -> {
                emitEvent(UiEvent.Navigate(route = Screens.Register.route))
            }
        }
    }

    private fun emitEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    private fun loginWithEmailAndPassword(email: String, password: String) {
        _emailError.value = null
        _passwordError.value = null

        if (email.isEmpty()) {
            _emailError.value = context.getString(R.string.empty_email_address_error)
            return
        }

        if (password.isEmpty()) {
            _passwordError.value = context.getString(R.string.empty_password_error)
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                try {
                    if (!task.isSuccessful)
                        throw task.exception!!
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    Log.d("firebase_auth", e.localizedMessage!!)
                    _passwordError.value = context.getString(R.string.auth_error_wrong_password)
                }
            }
    }

    private fun continueWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("firebaseAuthWithGoogle", "SUCCESS")
                } else {
                    Log.d("firebaseAuthWithGoogle", "FAILURE", task.exception)
                }
            }
    }
}