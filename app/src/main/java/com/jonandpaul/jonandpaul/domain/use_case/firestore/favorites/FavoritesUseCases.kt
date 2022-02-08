package com.jonandpaul.jonandpaul.domain.use_case.firestore.favorites

data class FavoritesUseCases(
    val getFavorites: GetFavorites,
    val deleteFavorite: DeleteFavorite,
    val insertFavorite: InsertFavorite
)
