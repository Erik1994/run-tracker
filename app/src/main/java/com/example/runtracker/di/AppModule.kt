package com.example.runtracker.di

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.runtracker.MainViewModel
import com.example.runtracker.app.RunnersApp
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

private const val PREF_NAME = "shared_pref"
val appModule = module {
    single<SharedPreferences> {
        EncryptedSharedPreferences(
            context = androidApplication(),
            fileName = PREF_NAME,
            masterKey = MasterKey(androidApplication()),
            prefKeyEncryptionScheme = EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            prefValueEncryptionScheme = EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    single<CoroutineScope> {
        (androidApplication() as RunnersApp).applicationScope
    }

    viewModelOf(::MainViewModel)
}