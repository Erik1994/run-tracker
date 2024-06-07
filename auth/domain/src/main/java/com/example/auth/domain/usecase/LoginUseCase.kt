package com.example.auth.domain.usecase

import com.example.auth.domain.repository.AuthRepository
import com.example.core.domain.util.DataError
import com.example.core.domain.util.EmptyResult

interface LoginUseCase {
    suspend operator fun invoke(email: String, password: String): EmptyResult<DataError.Network>
}

class LoginUseCaseImpl(
    private val authRepository: AuthRepository
) : LoginUseCase {
    override suspend fun invoke(email: String, password: String): EmptyResult<DataError.Network> {
        return authRepository.login(email = email, password = password)
    }
}