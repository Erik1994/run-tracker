package com.example.run.domain.usecase

import com.example.core.domain.run.RunId
import com.example.core.domain.run.RunRepository

interface DeleteRunUseCase {
    suspend operator fun invoke(id: RunId)
}


class DeleteRunUseCaseImpl(
    private val runRepository: RunRepository
) : DeleteRunUseCase {
    override suspend fun invoke(id: RunId) {
        runRepository.deleteRun(id = id)
    }
}