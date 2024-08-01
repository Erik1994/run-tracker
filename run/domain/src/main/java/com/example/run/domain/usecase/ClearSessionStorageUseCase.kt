package com.example.run.domain.usecase

import com.example.core.domain.auth.SessionStorage

interface ClearSessionStorageUseCase {
    suspend operator fun invoke()
}

class ClearSessionStorageUseCaseImpl(
    private val sessionStorage: SessionStorage
) : ClearSessionStorageUseCase {
    override suspend fun invoke() {
        sessionStorage.set(null)
    }
}
