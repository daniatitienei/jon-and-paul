package com.jonandpaul.jonandpaul.ui.screens.address

sealed class AddressEvents {
    object OnNavigationClick : AddressEvents()
    data class OnSaveClick(val newAddress: String) : AddressEvents()
}
