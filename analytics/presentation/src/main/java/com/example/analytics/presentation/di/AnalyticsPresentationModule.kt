package com.example.analytics.presentation.di

import com.example.analytics.domain.domain.GetAnalyticsValuesUseCase
import com.example.analytics.domain.domain.GetAnalyticsValuesUseCaseImpl
import com.example.analytics.presentation.AnalyticsDashboardViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val analyticsPresentationModule = module {
    singleOf(::GetAnalyticsValuesUseCaseImpl).bind<GetAnalyticsValuesUseCase>()
    viewModelOf(::AnalyticsDashboardViewModel)
}