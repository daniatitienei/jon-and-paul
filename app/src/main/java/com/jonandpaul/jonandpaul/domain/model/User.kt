package com.jonandpaul.jonandpaul.domain.model

data class User(
    val favorites: List<Product> = emptyList(),
)
