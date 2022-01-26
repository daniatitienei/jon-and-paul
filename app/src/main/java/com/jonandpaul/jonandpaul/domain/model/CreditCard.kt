package com.jonandpaul.jonandpaul.domain.model

data class CreditCard(
    val ownerName: String,
    val cvv: String,
    val cardNumber: String,
    val expirationMonth: Int,
    val expirationYear: Int
)
