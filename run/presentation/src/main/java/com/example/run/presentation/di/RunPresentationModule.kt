package com.example.run.presentation.di

import com.example.run.domain.RunningTracker
import com.example.run.domain.RunningTrackerImpl
import com.example.run.presentation.overview.RunOverviewViewModel
import com.example.run.presentation.tracking.RunTrackingViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val runsPresentationModule = module {
    singleOf(::RunningTrackerImpl).bind<RunningTracker>()

    viewModelOf(::RunOverviewViewModel)
    viewModelOf(::RunTrackingViewModel)
}