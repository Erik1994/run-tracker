package com.example.core.data.di

import com.example.core.data.auth.EncryptedSessionStorage
import com.example.core.data.dispathcers.AppDispatchers
import com.example.core.data.dispathcers.AppDispatchersImpl
import com.example.core.data.network.HttpClientFactory
import com.example.core.data.network.HttpClientFactoryImpl
import com.example.core.domain.auth.SessionStorage
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single<AppDispatchers> { AppDispatchersImpl }
    single<CoroutineDispatcher>(named(AppDispatchers.DISPATCHER_IO)) { AppDispatchersImpl.ioDispatcher }
    single<CoroutineDispatcher>(named(AppDispatchers.DISPATCHER_DEFAULT)) { AppDispatchersImpl.defaultDispatcher }
    single<CoroutineDispatcher>(named(AppDispatchers.DISPATCHER_UI)) { AppDispatchersImpl.mainDispatcher }
    single<SessionStorage> {
        EncryptedSessionStorage(
            sharedPreferences = get(),
            ioDispatcher = get(named(AppDispatchers.DISPATCHER_IO))
        )
    }
    singleOf(::HttpClientFactoryImpl).bind<HttpClientFactory>()
    single<HttpClient> { get<HttpClientFactory>().build() }
}