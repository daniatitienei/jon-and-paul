package com.jonandpaul.jonandpaul.domain.use_case.firestore.cart

data class CartUseCases(
    val getCartItems: GetCartItems,
    val insertCartItem: InsertCartItem,
    val deleteCartItem: DeleteCartItem,
    val updateQuantity: UpdateQuantity,
    val clearCart: ClearCart
)
