@file:Suppress("OPT_IN_USAGE_FUTURE_ERROR")
@file:OptIn(ExperimentalFoundationApi::class)

package com.example.auth.presentation.login

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(

) : ViewModel() {

    private val eventChannel = Channel<LoginEvents>()
    val events = eventChannel.receiveAsFlow()

    var state by mutableStateOf(LoginState())
        private set

    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.OnLoginClick -> {}
            LoginAction.OnRegisterClick -> navigateToRegister()
            LoginAction.OnTogglePasswordVisibilityClick -> {
                state = state.copy(isPasswordVisible = state.isPasswordVisible.not())
            }
        }
    }

    private fun navigateToRegister() {
        viewModelScope.launch {
            eventChannel.send(LoginEvents.RegisterNavigation)
        }
    }
}