package com.example.auth.presentation.register

import androidx.compose.runtime.Composable

@Composable
fun RegisterScreenRoot(
    onSignInClick: () -> Unit,
    onSuccessfulRegistration: () -> Unit,
    viewModel: RegisterViewModel
) {

    RegisterScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}