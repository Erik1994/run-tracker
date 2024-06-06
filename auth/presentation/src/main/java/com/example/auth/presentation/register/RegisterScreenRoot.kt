package com.example.auth.presentation.register

import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreenRoot(
    onSignInClick: () -> Unit,
    onSuccessfulRegistration: () -> Unit,
    viewModel: RegisterViewModel = koinViewModel()
) {

    RegisterScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}