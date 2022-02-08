package com.jonandpaul.jonandpaul.ui.screens.inspect_product

import com.jonandpaul.jonandpaul.domain.model.Product

data class InspectProductState(
    val suggestions: List<Product> = emptyList(),
    val favorites: List<Product> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)