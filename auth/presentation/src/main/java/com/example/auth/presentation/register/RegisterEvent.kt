package com.example.auth.presentation.register

import com.example.presentation.ui.UiText

sealed interface RegisterEvent {

    data object RegistrationSuccess : RegisterEvent

    data object LoginNavigation : RegisterEvent

    data class Error(val error: UiText) : RegisterEvent

}