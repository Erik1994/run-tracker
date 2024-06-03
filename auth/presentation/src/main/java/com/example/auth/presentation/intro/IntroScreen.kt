package com.example.auth.presentation.intro

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auth.presentation.R
import com.example.core.presentation.desygnsystem.LogoIcon
import com.example.core.presentation.desygnsystem.RunTrackerTheme
import com.example.core.presentation.desygnsystem.components.GradientBackground
import com.example.core.presentation.desygnsystem.components.RunnersActionButton
import com.example.core.presentation.desygnsystem.components.RunnersOutlinedActionButton
import com.example.core.presentation.desygnsystem.dimentions.LocalSpacing

@Composable
fun IntroScreen(
    onAction: (IntroAction) -> Unit
) {
    val spacing = LocalSpacing.current
    GradientBackground {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            RunnersLogoVertical()
        }
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.spaceMedium)
                .padding(bottom = spacing.spaceMediumLarge)
        ) {
            Text(
                text = stringResource(id = R.string.welcome_to_runners),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(spacing.spaceSmall))
            Text(
                text = stringResource(id = R.string.runners_description),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(spacing.spaceLarge))
            RunnersOutlinedActionButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(id = R.string.sign_in),
                isLoading = false,
                onClick = {
                    onAction(IntroAction.OnSingInClick)
                }
            )
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            RunnersActionButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(id = R.string.sign_up),
                isLoading = false,
                onClick = {
                    onAction(IntroAction.OnSignUpClick)
                }
            )
        }
    }
}

@Composable
private fun RunnersLogoVertical(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = LogoIcon,
            contentDescription = "Logo",
            tint = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(id = R.string.runners),
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview
@Composable
private fun IntroScreenPreview() {
    RunTrackerTheme {
        IntroScreen(
            onAction = {}
        )
    }
}