package com.example.wear.app.presentation.di

import com.example.wear.app.presentation.RunnersApp
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single<CoroutineScope> {
        (androidApplication() as RunnersApp).applicationScope
    }
}