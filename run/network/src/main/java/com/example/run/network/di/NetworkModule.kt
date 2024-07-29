package com.example.run.network.di

import com.example.core.domain.run.RemoteRunDataSource
import com.example.run.network.KtorRemoteRunDatasource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val networkModule = module {
    singleOf(::KtorRemoteRunDatasource).bind<RemoteRunDataSource>()
}