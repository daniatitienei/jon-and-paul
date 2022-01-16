package com.jonandpaul.jonandpaul.ui.utils

sealed class UiEvent {
    data class BackDropScaffold(val isOpen: Boolean): UiEvent()

    data class Navigate(val route: String): UiEvent()
    object PopBackStack: UiEvent()
}
