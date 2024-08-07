package com.example.auth.data.di

import com.example.auth.data.EmailPatternValidator
import com.example.auth.data.repository.AuthRepositoryImpl
import com.example.auth.domain.PatternValidator
import com.example.auth.domain.UserDataValidator
import com.example.auth.domain.UserDataValidatorImpl
import com.example.auth.domain.repository.AuthRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    singleOf(::EmailPatternValidator).bind<PatternValidator>()
    singleOf(::UserDataValidatorImpl).bind<UserDataValidator>()
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
}