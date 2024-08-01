package com.example.run.domain.usecase

import com.example.core.domain.run.RunRepository

interface DeleteAllRunsUseCase {
    suspend operator fun invoke()
}


class DeleteAllRunsUseCaseImpl(
    private val runRepository: RunRepository
) : DeleteAllRunsUseCase {
    override suspend fun invoke() {
        runRepository.deleteAllRuns()
    }
}