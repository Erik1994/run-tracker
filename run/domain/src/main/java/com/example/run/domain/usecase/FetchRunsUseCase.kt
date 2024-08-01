package com.example.run.domain.usecase

import com.example.core.domain.run.RunRepository
import com.example.core.domain.util.DataError
import com.example.core.domain.util.EmptyResult

interface FetchRunsUseCase {
    suspend operator fun invoke(): EmptyResult<DataError>
}


class FetchRunsUseCaseImpl(
    private val runRepository: RunRepository
) : FetchRunsUseCase {
    override suspend fun invoke(): EmptyResult<DataError> {
        return runRepository.fetchRuns()
    }
}