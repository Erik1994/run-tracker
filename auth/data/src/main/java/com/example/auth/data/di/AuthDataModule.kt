package com.example.auth.data.di

import com.example.auth.data.EmailPatternValidator
import com.example.auth.domain.PatternValidator
import com.example.auth.domain.UserDataValidator
import com.example.auth.domain.UserDataValidatorImpl
import org.koin.dsl.module

val authDataModule = module {
    single<PatternValidator> {
        EmailPatternValidator()
    }
    single<UserDataValidator> {
        UserDataValidatorImpl(patternValidator = get())
    }
}