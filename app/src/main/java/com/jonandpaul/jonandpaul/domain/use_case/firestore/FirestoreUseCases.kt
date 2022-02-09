package com.jonandpaul.jonandpaul.domain.use_case.firestore

import com.jonandpaul.jonandpaul.domain.use_case.firestore.favorites.FavoritesUseCases
import com.jonandpaul.jonandpaul.domain.use_case.firestore.products.GetProducts
import com.jonandpaul.jonandpaul.domain.use_case.firestore.products.GetSuggestions

data class FirestoreUseCases(
    val favorites: FavoritesUseCases,
    val getProducts: GetProducts,
    val getSuggestions: GetSuggestions
)