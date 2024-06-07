package com.example.auth.presentation.login

import com.example.presentation.ui.UiText

sealed interface LoginEvents {

    data object LoginSuccess : LoginEvents

    data object RegisterNavigation : LoginEvents

    data class Error(val error: UiText) : LoginEvents
}