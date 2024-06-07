package com.example.auth.presentation.login

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.example.auth.presentation.R
import com.example.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreenRoot(
    onRegisterClick: () -> Unit,
    onSuccessfulLogin: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    ObserveAsEvents(flow = viewModel.events) { event ->
        when(event) {
            is LoginEvents.Error -> {
                keyboardController?.hide()
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
            LoginEvents.RegisterNavigation -> onRegisterClick()
            LoginEvents.LoginSuccess -> {
                keyboardController?.hide()
                Toast.makeText(
                    context,
                    R.string.youre_logged_in,
                    Toast.LENGTH_LONG
                ).show()
                onSuccessfulLogin()
            }
        }
    }

    LoginScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}