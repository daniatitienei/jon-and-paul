package com.jonandpaul.jonandpaul.ui.screens.account

sealed class AccountEvents {
    object OnOrdersClick : AccountEvents()
    object OnPopBackStack : AccountEvents()
    data class OnAlertDialog(val isOpen: Boolean) : AccountEvents()

    data class OnSendFeedback(val message: String) : AccountEvents()
}