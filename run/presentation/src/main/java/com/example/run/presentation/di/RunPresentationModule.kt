package com.example.run.presentation.di

import com.example.run.presentation.overview.RunOverviewViewModel
import com.example.run.presentation.tracking.RunTrackingViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val runsPresentationModule = module {
    viewModelOf(::RunOverviewViewModel)
    viewModelOf(::RunTrackingViewModel)
}