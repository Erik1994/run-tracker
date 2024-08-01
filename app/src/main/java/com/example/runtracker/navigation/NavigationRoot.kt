package com.example.runtracker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import com.example.auth.presentation.intro.IntroScreenRoot
import com.example.auth.presentation.login.LoginScreenRoot
import com.example.auth.presentation.register.RegisterScreenRoot
import com.example.run.presentation.overview.RunOverviewScreenRoot
import com.example.run.presentation.tracking.RunTrackingScreenRoot
import com.example.run.presentation.tracking.service.RunTrackingService
import com.example.run.presentation.tracking.service.RunTrackingService.Companion.RUNNERS_DEEP_LINK
import com.example.runtracker.MainActivity

@Composable
fun NavigationRoot(
    navController: NavHostController,
    isLoggedIn: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Route.RUN else Route.AUTH
    ) {
        authGraph(navController)
        runGraph(navController)
    }
}

private fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation(
        startDestination = Route.INTRO,
        route = Route.AUTH
    ) {
        composable(route = Route.INTRO) {
            IntroScreenRoot(
                onSignInClick = { navController.navigate(Route.LOGIN) },
                onSignUpClick = { navController.navigate(Route.REGISTER) }
            )
        }
        composable(route = Route.REGISTER) {
            RegisterScreenRoot(
                onSignInClick = {
                    navController.navigate(Route.LOGIN) {
                        popUpTo(Route.REGISTER) {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                onSuccessfulRegistration = { navController.navigate(Route.LOGIN) },
            )
        }
        composable(route = Route.LOGIN) {
            LoginScreenRoot(
                onRegisterClick = {
                    navController.navigate(Route.REGISTER) {
                        popUpTo(Route.LOGIN) {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                onSuccessfulLogin = {
                    navController.navigate(Route.RUN) {
                        popUpTo(Route.AUTH) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

private fun NavGraphBuilder.runGraph(navController: NavHostController) {
    navigation(
        startDestination = Route.RUN_OVERVIEW,
        route = Route.RUN
    ) {
        composable(route = Route.RUN_OVERVIEW) {
            RunOverviewScreenRoot(onStartRunClick = {
                navController.navigate(Route.RUN_TRACKING)
            })
        }
        composable(
            route = Route.RUN_TRACKING,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = RUNNERS_DEEP_LINK
                }
            )
        ) {
            val context = LocalContext.current
            RunTrackingScreenRoot(
                onServiceToggle = {
                    when (it) {
                        RunTrackingService.RunTrackingServiceState.START -> context.startService(
                            RunTrackingService.createStartIntent(
                                context = context,
                                activityClass = MainActivity::class.java
                            )
                        )

                        RunTrackingService.RunTrackingServiceState.STOP -> context.startService(
                            RunTrackingService.createStopIntent(context = context)
                        )
                    }
                },
                onFinish = { navController.navigateUp() },
                onBack = { navController.navigateUp() }
            )
        }
    }
}
