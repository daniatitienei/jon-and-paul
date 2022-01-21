package com.jonandpaul.jonandpaul.ui.screens.register

sealed class RegisterEvents {
    data class OnRegisterClick(
        val email: String,
        val password: String
    ) : RegisterEvents()

    object OnGoogleClick: RegisterEvents()
    object OnNavigationIconClick: RegisterEvents()
    object OnAlreadyHaveAnAccount: RegisterEvents()
}
