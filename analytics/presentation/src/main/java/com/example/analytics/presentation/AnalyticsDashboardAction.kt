package com.example.analytics.presentation

sealed interface AnalyticsDashboardAction {
    data object OnBackClick: AnalyticsDashboardAction
}