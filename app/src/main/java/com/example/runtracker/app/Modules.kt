package com.example.runtracker.app

import com.example.auth.data.di.authDataModule
import com.example.auth.presentation.di.authViewModelModule
import com.example.core.data.di.coreDataModule
import com.example.runtracker.di.appModule

object Modules {
    val modules = arrayOf(
        appModule,
        coreDataModule,
        authDataModule,
        authViewModelModule
    )
}