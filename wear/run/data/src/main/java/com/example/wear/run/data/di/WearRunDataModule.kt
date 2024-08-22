package com.example.wear.run.data.di

import com.example.core.data.dispathcers.AppDispatchersImpl
import com.example.core.domain.dispatchers.AppDispatchers
import com.example.wear.run.data.HealthServicesExerciseTracker
import com.example.wear.run.data.connectivity.WatchToPhoneConnector
import com.example.wear.run.domain.ExerciseTracker
import com.example.wear.run.domain.PhoneConnector
import com.example.wear.run.domain.RunningTracker
import com.example.wear.run.domain.RunningTrackerImpl
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val wearRunDataModule = module {
    singleOf(::HealthServicesExerciseTracker).bind<ExerciseTracker>()
    singleOf(::WatchToPhoneConnector).bind<PhoneConnector>()
    singleOf(::RunningTrackerImpl).bind<RunningTracker>()
    single<AppDispatchers> { AppDispatchersImpl }
    single<CoroutineDispatcher>(named(AppDispatchers.DISPATCHER_IO)) { AppDispatchersImpl.ioDispatcher }
    single<CoroutineDispatcher>(named(AppDispatchers.DISPATCHER_DEFAULT)) { AppDispatchersImpl.defaultDispatcher }
    single<CoroutineDispatcher>(named(AppDispatchers.DISPATCHER_UI)) { AppDispatchersImpl.mainDispatcher }
    single {
        get<RunningTracker>().elapsedTime
    }
}