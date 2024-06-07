@file:Suppress("OPT_IN_USAGE_FUTURE_ERROR")
@file:OptIn(ExperimentalFoundationApi::class)

package com.example.auth.presentation.login

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.auth.presentation.R
import com.example.auth.presentation.register.RegisterAction
import com.example.core.presentation.desygnsystem.EmailIcon
import com.example.core.presentation.desygnsystem.Poppins
import com.example.core.presentation.desygnsystem.RunnersGray
import com.example.core.presentation.desygnsystem.RunnersTheme
import com.example.core.presentation.desygnsystem.components.GradientBackground
import com.example.core.presentation.desygnsystem.components.RunnersActionButton
import com.example.core.presentation.desygnsystem.components.RunnersPasswordTextField
import com.example.core.presentation.desygnsystem.components.RunnersTextField
import com.example.core.presentation.desygnsystem.dimentions.LocalDimensions

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit
) {
    val spacing = LocalDimensions.current

    GradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = spacing.dimenMedium)
                .padding(vertical = spacing.dimenLarge)
                .padding(top = spacing.dimenDefault)
        ) {
            Text(
                text = stringResource(id = R.string.hi_there),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = stringResource(id = R.string.runners_welcome_text),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(spacing.dimenMediumLarge))

            RunnersTextField(
                state = state.email,
                startIcon = EmailIcon,
                endIcon = null,
                hint = stringResource(id = R.string.example_email),
                title = stringResource(id = R.string.email),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(spacing.dimenMedium))

            RunnersPasswordTextField(
                state = state.password,
                hint = stringResource(id = R.string.password),
                title = stringResource(id = R.string.password),
                isPasswordVisible = state.isPasswordVisible,
                onTogglePasswordVisibility = {
                    onAction(LoginAction.OnTogglePasswordVisibilityClick)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(spacing.dimenLarge))

            RunnersActionButton(
                text = stringResource(id = R.string.login),
                isLoading = state.isLoggingIn,
                enabled = state.canLogin,
                onClick = { onAction(LoginAction.OnLoginClick) },
                modifier = Modifier.fillMaxWidth()
            )

            val annotatedString = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontFamily = Poppins,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    append(stringResource(id = R.string.dont_have_an_account) + " ")
                    //makes login text clickable
                    pushStringAnnotation(
                        tag = "clickable_sign_up_text",
                        annotation = stringResource(id = R.string.sign_up)
                    )
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = Poppins
                        )
                    ) {
                        append(stringResource(id = R.string.sign_up))
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.BottomCenter
            ) {

                ClickableText(
                    text = annotatedString,
                    onClick = { offset ->
                        annotatedString.getStringAnnotations(
                            tag = "clickable_sign_up_text",
                            start = offset,
                            end = offset
                        ).firstOrNull()?.let {
                            onAction(LoginAction.OnRegisterClick)
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    RunnersTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {}
        )
    }
}