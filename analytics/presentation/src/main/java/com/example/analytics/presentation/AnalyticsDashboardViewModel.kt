package com.example.analytics.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.analytics.domain.domain.GetAnalyticsValuesUseCase
import com.example.analytics.presentation.mappers.toAnalyticsDashboardState
import kotlinx.coroutines.launch

class AnalyticsDashboardViewModel(
    private val getAnalyticsValuesUseCase: GetAnalyticsValuesUseCase
): ViewModel() {

    var state by mutableStateOf<AnalyticsDashboardState?>(null)
        private set

    init {
        viewModelScope.launch {
            state = getAnalyticsValuesUseCase().toAnalyticsDashboardState()
        }
    }

}