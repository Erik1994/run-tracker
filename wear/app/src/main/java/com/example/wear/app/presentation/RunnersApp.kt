package com.example.wear.app.presentation

import android.app.Application
import com.example.wear.run.presentation.di.runPresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class RunnersApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
           androidLogger()
           androidContext(this@RunnersApp)
            modules(runPresentationModule)
        }
    }
}