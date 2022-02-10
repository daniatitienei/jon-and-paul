package com.jonandpaul.jonandpaul.ui.utils

import com.jonandpaul.jonandpaul.domain.model.Product

sealed class UiEvent {
    object BackdropScaffold : UiEvent()
    object ModalBottomSheet : UiEvent()
    data class Toast(val message: String) : UiEvent()
    data class AlertDialog(val isOpen: Boolean) : UiEvent()

    data class Navigate(val route: String, val popUpTo: String? = null) : UiEvent()
    object PopBackStack : UiEvent()
}
