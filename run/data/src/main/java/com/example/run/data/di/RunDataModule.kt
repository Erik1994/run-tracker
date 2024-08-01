package com.example.run.data.di

import com.example.run.data.worker.CreateRunWorker
import com.example.run.data.worker.DeleteRunWorker
import com.example.run.data.worker.FetchRunsWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module


val runDataModule = module {
    workerOf(::CreateRunWorker)
    workerOf(::DeleteRunWorker)
    workerOf(::FetchRunsWorker)
}