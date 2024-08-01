package com.example.auth.presentation.di

import com.example.auth.domain.usecase.LoginUseCase
import com.example.auth.domain.usecase.LoginUseCaseImpl
import com.example.auth.domain.usecase.RegisterUseCase
import com.example.auth.domain.usecase.RegisterUseCaseImpl
import com.example.auth.presentation.login.LoginViewModel
import com.example.auth.presentation.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authPresentationModule = module {
    singleOf(::RegisterUseCaseImpl).bind<RegisterUseCase>()
    singleOf(::LoginUseCaseImpl).bind<LoginUseCase>()
    viewModelOf(::RegisterViewModel)
    viewModelOf(::LoginViewModel)
}