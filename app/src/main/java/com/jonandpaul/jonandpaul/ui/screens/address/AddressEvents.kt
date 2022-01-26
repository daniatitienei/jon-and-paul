package com.jonandpaul.jonandpaul.ui.screens.address

sealed class AddressEvents {
    object OnNavigationClick : AddressEvents()
    object OnSaveClick : AddressEvents()
}
