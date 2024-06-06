package com.example.auth.domain.usecase

import com.example.auth.domain.repository.AuthRepository
import com.example.core.domain.util.DataError
import com.example.core.domain.util.EmptyResult

interface RegisterUseCase {
    suspend operator fun invoke(email: String, password: String): EmptyResult<DataError.Network>
}

class RegisterUseCaseImpl(private val authRepository: AuthRepository) : RegisterUseCase {
    override suspend fun invoke(email: String, password: String): EmptyResult<DataError.Network> {
        return authRepository.register(email = email, password = password)
    }
}