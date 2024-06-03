package com.example.auth.presentation.intro

import androidx.compose.runtime.Composable

@Composable
fun IntroScreenRoot(
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
) {
    IntroScreen(
        onAction = {
            when (it) {
                IntroAction.OnSignUpClick -> onSignUpClick()
                IntroAction.OnSingInClick -> onSignInClick()
            }
        }
    )
}