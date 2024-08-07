package com.example.wear.run.data.di

import com.example.core.domain.dispatchers.AppDispatchers
import com.example.wear.run.data.HealthServicesExerciseTracker
import com.example.wear.run.data.connectivity.WatchToPhoneConnector
import com.example.wear.run.domain.ExerciseTracker
import com.example.wear.run.domain.PhoneConnector
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val wearRunDataModule = module {
    single<ExerciseTracker> {
        HealthServicesExerciseTracker(
            context = get(),
            ioDispatcher = get(named(AppDispatchers.DISPATCHER_IO))
        )
    }
    singleOf(::WatchToPhoneConnector).bind<PhoneConnector>()
}