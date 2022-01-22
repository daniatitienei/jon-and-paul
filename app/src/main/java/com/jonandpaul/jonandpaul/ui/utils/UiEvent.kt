package com.jonandpaul.jonandpaul.ui.utils

import com.jonandpaul.jonandpaul.domain.model.Product

sealed class UiEvent {
    object BackdropScaffold : UiEvent()
    object BottomSheet : UiEvent()
    object ModalBottomSheet : UiEvent()

    data class Navigate(val route: String) : UiEvent()
    object PopBackStack : UiEvent()
}
