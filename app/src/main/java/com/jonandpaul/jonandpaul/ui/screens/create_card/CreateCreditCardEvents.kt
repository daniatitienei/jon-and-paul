package com.jonandpaul.jonandpaul.ui.screens.create_card

sealed class CreateCreditCardEvents {
    data class OnAddCard(
        val owner: String,
        val number: String,
        val cvv: String,
        val expirationDate: String
    ) : CreateCreditCardEvents()

    object OnNavigationClick : CreateCreditCardEvents()
}