package com.example.runtracker.di

import com.example.auth.data.di.authDataModule
import com.example.auth.presentation.di.authPresentationModule
import com.example.core.data.di.coreDataModule
import com.example.core.database.di.databaseModule
import com.example.run.location.di.locationModule
import com.example.run.network.di.networkModule
import com.example.run.presentation.di.runsPresentationModule

object Modules {
    val modules = arrayOf(
        appModule,
        coreDataModule,
        authDataModule,
        authPresentationModule,
        runsPresentationModule,
        locationModule,
        databaseModule,
        networkModule
    )
}