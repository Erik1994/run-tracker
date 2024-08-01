package com.example.run.domain.usecase

import com.example.core.domain.run.Run
import com.example.core.domain.run.RunRepository
import com.example.core.domain.util.DataError
import com.example.core.domain.util.EmptyResult

interface UpsertRunUseCase {
    suspend operator fun invoke(run: Run, mapPicture: ByteArray): EmptyResult<DataError>
}


class UpsertRunUseCaseImpl(
    private val runRepository: RunRepository
) : UpsertRunUseCase {
    override suspend fun invoke(run: Run, mapPicture: ByteArray): EmptyResult<DataError> {
        return runRepository.upsertRun(
            run = run,
            mapPicture = mapPicture
        )
    }
}