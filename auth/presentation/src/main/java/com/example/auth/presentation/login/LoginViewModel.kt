@file:Suppress("OPT_IN_USAGE_FUTURE_ERROR")
@file:OptIn(ExperimentalFoundationApi::class)

package com.example.auth.presentation.login

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text2.input.textAsFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth.domain.UserDataValidator
import com.example.auth.domain.usecase.LoginUseCase
import com.example.auth.presentation.R
import com.example.core.domain.util.DataError
import com.example.core.domain.util.Result
import com.example.presentation.ui.UiText
import com.example.presentation.ui.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val userDataValidator: UserDataValidator
) : ViewModel() {

    private val eventChannel = Channel<LoginEvents>()
    val events = eventChannel.receiveAsFlow()

    var state by mutableStateOf(LoginState())
        private set

    init {
        combine(state.email.textAsFlow(), state.password.textAsFlow()) { email, password ->
            state = state.copy(
                canLogin = userDataValidator.isValidEmail(
                    email = email.toString().trim()
                ) && password.isNotEmpty() && state.isLoggingIn.not()
            )
        }.launchIn(viewModelScope)
    }

    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.OnLoginClick -> login()
            LoginAction.OnRegisterClick -> navigateToRegister()
            LoginAction.OnTogglePasswordVisibilityClick -> {
                state = state.copy(isPasswordVisible = state.isPasswordVisible.not())
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            state = state.copy(isLoggingIn = true)
            val result = loginUseCase(
                email = state.email.text.toString().trim(),
                password = state.password.text.toString()
            )
            state = state.copy(isLoggingIn = false)
            when(result) {
                is Result.Error -> {
                    if (result.error == DataError.Network.UNAUTHORIZED) {
                        eventChannel.send(LoginEvents.Error(
                            error = UiText.StringResource(id = R.string.error_email_password_incorrect)
                        ))
                    } else {
                        eventChannel.send(LoginEvents.Error(error = result.error.asUiText()))
                    }
                }
                is Result.Success -> eventChannel.send(LoginEvents.LoginSuccess)
            }
        }
    }

    private fun navigateToRegister() {
        viewModelScope.launch {
            eventChannel.send(LoginEvents.RegisterNavigation)
        }
    }
}