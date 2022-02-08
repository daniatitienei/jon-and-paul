package com.jonandpaul.jonandpaul.domain.use_case.firestore.favorites

import com.jonandpaul.jonandpaul.domain.model.Product
import com.jonandpaul.jonandpaul.domain.repository.FavoritesRepository
import com.jonandpaul.jonandpaul.ui.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetFavorites(
    private val repository: FavoritesRepository
) {
    operator fun invoke(): Flow<Resource<List<Product>>> = repository.getFavorites()

}