package com.jonandpaul.jonandpaul.ui.utils

import com.jonandpaul.jonandpaul.domain.model.Product

sealed class UiEvent {
    object BackdropScaffold : UiEvent()
    object ModalBottomSheet : UiEvent()
    object Toast : UiEvent()

    data class Navigate(val route: String, val popUpTo: String? = null) : UiEvent()
    object PopBackStack : UiEvent()
}
