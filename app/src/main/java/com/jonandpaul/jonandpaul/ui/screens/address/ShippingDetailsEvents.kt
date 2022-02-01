package com.jonandpaul.jonandpaul.ui.screens.address

import com.jonandpaul.jonandpaul.domain.model.ShippingDetails

sealed class ShippingDetailsEvents {
    object OnNavigationClick : ShippingDetailsEvents()
    data class OnSaveClick(val shippingDetails: ShippingDetails) : ShippingDetailsEvents()
}
