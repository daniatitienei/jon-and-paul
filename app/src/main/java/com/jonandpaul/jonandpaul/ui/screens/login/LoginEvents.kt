package com.jonandpaul.jonandpaul.ui.screens.login

sealed class LoginEvents {
    object OnRegisterHereClick: LoginEvents()
    object OnPopBackStack : LoginEvents()
    data class OnLoginClick(val email: String, val password: String) : LoginEvents()
    data class OnContinueWithGoogle(val idToken: String) : LoginEvents()
}
