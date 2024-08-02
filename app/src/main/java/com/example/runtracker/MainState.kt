package com.example.runtracker

data class MainState(
    val isLoggedIn: Boolean = false,
    val isCheckingAuth: Boolean = false,
    val showAnalyticsInstallDialog: Boolean = false,
    val analyticsFeatureModuleState: String = ""
)
