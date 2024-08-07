package com.example.run.data.di

import com.example.core.domain.run.SyncRunScheduler
import com.example.run.data.connectivity.PhoneToWatchConnector
import com.example.run.data.worker.CreateRunWorker
import com.example.run.data.worker.DeleteRunWorker
import com.example.run.data.worker.FetchRunsWorker
import com.example.run.data.worker.SyncRunWorkersScheduler
import com.example.run.domain.WatchConnector
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val runDataModule = module {
    workerOf(::CreateRunWorker)
    workerOf(::DeleteRunWorker)
    workerOf(::FetchRunsWorker)
    singleOf(::SyncRunWorkersScheduler).bind<SyncRunScheduler>()
    singleOf(::PhoneToWatchConnector).bind<WatchConnector>()
}