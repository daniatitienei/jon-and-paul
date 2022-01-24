package com.jonandpaul.jonandpaul.domain.model

data class User(
    val cart: MutableList<CartProduct>? = null
)
