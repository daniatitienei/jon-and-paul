package com.jonandpaul.jonandpaul.domain.model

data class ShippingDetails(
    val firstName: String = "",
    val lastName: String = "",
    val address: String = "",
    val county: String = "",
    val city: String = "",
    val postalCode: String = "",
    val phone: String = "",
)
