package com.example.analytics.domain.domain

import com.example.analytics.domain.AnalyticsRepository
import com.example.analytics.domain.AnalyticsValues

interface GetAnalyticsValuesUseCase {
    suspend operator fun invoke(): AnalyticsValues
}

class GetAnalyticsValuesUseCaseImpl(
    private val analyticsRepository: AnalyticsRepository
): GetAnalyticsValuesUseCase {
    override suspend fun invoke(): AnalyticsValues {
        return analyticsRepository.getAnalyticsValues()
    }
}
