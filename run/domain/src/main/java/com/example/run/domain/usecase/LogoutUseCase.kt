package com.example.run.domain.usecase

import com.example.core.domain.run.RunRepository
import com.example.core.domain.util.DataError
import com.example.core.domain.util.EmptyResult

interface LogoutUseCase {
    suspend operator fun invoke(): EmptyResult<DataError.Network>
}

class LogoutUseCaseImpl(
    private val runRepository: RunRepository
) : LogoutUseCase {
    override suspend fun invoke(): EmptyResult<DataError.Network> {
        return runRepository.logOut()
    }
}