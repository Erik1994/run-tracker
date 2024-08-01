package com.example.run.domain.usecase

import com.example.core.domain.run.RunRepository

interface SyncPendingRunsUseCase {
    suspend operator fun invoke()
}


class SyncPendingRunsUseCaseImpl(
    private val runRepository: RunRepository
): SyncPendingRunsUseCase {
    override suspend fun invoke() {
        runRepository.syncPendingRuns()
    }
}