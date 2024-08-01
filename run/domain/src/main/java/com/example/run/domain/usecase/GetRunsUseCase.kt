package com.example.run.domain.usecase

import com.example.core.domain.run.Run
import com.example.core.domain.run.RunRepository
import kotlinx.coroutines.flow.Flow

interface GetRunsUseCase {
    operator fun invoke(): Flow<List<Run>>
}


class GetRunsUseCaseImpl(
    private val runRepository: RunRepository
) : GetRunsUseCase {
    override fun invoke(): Flow<List<Run>> {
        return runRepository.getRuns()
    }
}