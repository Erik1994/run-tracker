package com.example.run.presentation.di

import com.example.run.domain.RunningTracker
import com.example.run.domain.RunningTrackerImpl
import com.example.run.domain.usecase.ClearSessionStorageUseCase
import com.example.run.domain.usecase.ClearSessionStorageUseCaseImpl
import com.example.run.domain.usecase.DeleteAllRunsUseCase
import com.example.run.domain.usecase.DeleteAllRunsUseCaseImpl
import com.example.run.domain.usecase.DeleteRunUseCase
import com.example.run.domain.usecase.DeleteRunUseCaseImpl
import com.example.run.domain.usecase.FetchRunsUseCase
import com.example.run.domain.usecase.FetchRunsUseCaseImpl
import com.example.run.domain.usecase.GetRunsUseCase
import com.example.run.domain.usecase.GetRunsUseCaseImpl
import com.example.run.domain.usecase.LogoutUseCase
import com.example.run.domain.usecase.LogoutUseCaseImpl
import com.example.run.domain.usecase.SyncPendingRunsUseCase
import com.example.run.domain.usecase.SyncPendingRunsUseCaseImpl
import com.example.run.domain.usecase.UpsertRunUseCase
import com.example.run.domain.usecase.UpsertRunUseCaseImpl
import com.example.run.presentation.overview.RunOverviewViewModel
import com.example.run.presentation.tracking.RunTrackingViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val runsPresentationModule = module {
    singleOf(::RunningTrackerImpl).bind<RunningTracker>()
    single {
        get<RunningTracker>().elapsedTime
    }
    singleOf(::GetRunsUseCaseImpl).bind<GetRunsUseCase>()
    singleOf(::FetchRunsUseCaseImpl).bind<FetchRunsUseCase>()
    singleOf(::UpsertRunUseCaseImpl).bind<UpsertRunUseCase>()
    singleOf(::DeleteRunUseCaseImpl).bind<DeleteRunUseCase>()
    singleOf(::SyncPendingRunsUseCaseImpl).bind<SyncPendingRunsUseCase>()
    singleOf(::LogoutUseCaseImpl).bind<LogoutUseCase>()
    singleOf(::DeleteAllRunsUseCaseImpl).bind<DeleteAllRunsUseCase>()
    singleOf(::ClearSessionStorageUseCaseImpl).bind<ClearSessionStorageUseCase>()

    viewModelOf(::RunOverviewViewModel)
    viewModelOf(::RunTrackingViewModel)
}