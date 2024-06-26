package com.example.runtracker.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.auth.presentation.intro.IntroScreenRoot
import com.example.auth.presentation.login.LoginScreenRoot
import com.example.auth.presentation.register.RegisterScreenRoot
import com.example.run.presentation.overview.RunOverviewScreenRoot
import com.example.run.presentation.tracking.RunTrackingScreenRoot

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
        composable(route = Route.RUN_TRACKING) {
            RunTrackingScreenRoot()
        }
    }
}
