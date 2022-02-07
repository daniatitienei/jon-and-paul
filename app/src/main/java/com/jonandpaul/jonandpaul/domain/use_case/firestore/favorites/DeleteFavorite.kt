package com.jonandpaul.jonandpaul.domain.use_case.firestore.favorites

import com.jonandpaul.jonandpaul.domain.model.Product
import com.jonandpaul.jonandpaul.domain.repository.FavoritesRepository

class DeleteFavorite(
    private val repository: FavoritesRepository
) {
    operator fun invoke(product: Product) = repository.removeFavorite(product = product)
}