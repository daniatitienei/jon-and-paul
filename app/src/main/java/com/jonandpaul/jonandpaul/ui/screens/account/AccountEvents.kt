package com.jonandpaul.jonandpaul.ui.screens.account

sealed class AccountEvents {
    object OnOrdersClick : AccountEvents()
    object OnCreditCardClick : AccountEvents()
    object OnInfoClick : AccountEvents()
    object OnLogoutClick : AccountEvents()

    object OnPopBackStack : AccountEvents()
}