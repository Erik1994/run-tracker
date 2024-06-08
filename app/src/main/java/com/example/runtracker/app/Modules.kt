package com.example.runtracker.app

import com.example.auth.data.di.authDataModule
import com.example.auth.presentation.di.authPresentationModule
import com.example.core.data.di.coreDataModule
import com.example.run.presentation.di.runsPresentationModule
import com.example.runtracker.di.appModule

object Modules {
    val modules = arrayOf(
        appModule,
        coreDataModule,
        authDataModule,
        authPresentationModule,
        runsPresentationModule
    )
}