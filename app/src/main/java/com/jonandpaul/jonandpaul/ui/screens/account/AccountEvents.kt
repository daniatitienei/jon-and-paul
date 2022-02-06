package com.jonandpaul.jonandpaul.ui.screens.account

sealed class AccountEvents {
    object OnOrdersClick : AccountEvents()
    object OnPopBackStack : AccountEvents()
}