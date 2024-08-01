package com.example.run.presentation.overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.run.SyncRunScheduler
import com.example.run.domain.usecase.DeleteRunUseCase
import com.example.run.domain.usecase.FetchRunsUseCase
import com.example.run.domain.usecase.GetRunsUseCase
import com.example.run.domain.usecase.SyncPendingRunsUseCase
import com.example.run.presentation.tracking.mapper.toRunUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

class RunOverviewViewModel(
    private val getRunsUseCase: GetRunsUseCase,
    private val fetchRunsUseCase: FetchRunsUseCase,
    private val deleteRunUseCase: DeleteRunUseCase,
    private val syncPendingRunsUseCase: SyncPendingRunsUseCase,
    private val syncRunScheduler: SyncRunScheduler
) : ViewModel() {

    var state by mutableStateOf(RunOverviewState())
        private set

    private val channelEvents = Channel<RunOverviewEvent>()
    val events = channelEvents.receiveAsFlow()

    init {

        viewModelScope.launch {
            syncRunScheduler.scheduleSync(
                type = SyncRunScheduler.SyncType.FetchRuns(30.minutes)
            )
        }

        getRunsUseCase()
            .onEach { runs ->
                val runsUi = runs.map { it.toRunUi() }
                state = state.copy(runs = runsUi)
            }.launchIn(viewModelScope)
        viewModelScope.launch {
            syncPendingRunsUseCase()
            fetchRunsUseCase()
        }
    }

    fun onAction(action: RunOverviewAction) {
        when (action) {
            RunOverviewAction.OnAnalyticsClick -> {}
            RunOverviewAction.OnLogoutClick -> {}
            RunOverviewAction.OnStartClick -> navigateToRunTracking()
            is RunOverviewAction.DeleteRun -> viewModelScope.launch {
                deleteRunUseCase(action.runUi.id)
            }
        }
    }

    private fun navigateToRunTracking() {
        viewModelScope.launch {
            channelEvents.send(RunOverviewEvent.RunTrackingNavigation)
        }
    }
}