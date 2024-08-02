package com.example.run.presentation.overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.run.SyncRunScheduler
import com.example.run.domain.usecase.ClearSessionStorageUseCase
import com.example.run.domain.usecase.DeleteAllRunsUseCase
import com.example.run.domain.usecase.DeleteRunUseCase
import com.example.run.domain.usecase.FetchRunsUseCase
import com.example.run.domain.usecase.GetRunsUseCase
import com.example.run.domain.usecase.LogoutUseCase
import com.example.run.domain.usecase.SyncPendingRunsUseCase
import com.example.run.presentation.tracking.mapper.toRunUi
import kotlinx.coroutines.CoroutineScope
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
    private val syncRunScheduler: SyncRunScheduler,
    private val logoutUseCase: LogoutUseCase,
    private val deleteAllRunsUseCase: DeleteAllRunsUseCase,
    private val applicationScope: CoroutineScope,
    private val clearSessionStorageUseCase: ClearSessionStorageUseCase
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
            RunOverviewAction.OnAnalyticsClick -> handleEvent(RunOverviewEvent.AnalyticsNavigationEvent)
            RunOverviewAction.OnLogoutClick -> {
                logOut()
                handleEvent(RunOverviewEvent.AuthNavigationEvent)
            }
            RunOverviewAction.OnStartClick -> handleEvent(RunOverviewEvent.RunTrackingNavigationEvent)
            is RunOverviewAction.DeleteRun -> viewModelScope.launch {
                deleteRunUseCase(action.runUi.id)
            }
        }
    }

    private fun handleEvent(event: RunOverviewEvent) {
        viewModelScope.launch {
            channelEvents.send(event)
        }
    }

    private fun logOut() {
        applicationScope.launch {
            syncRunScheduler.cancelAllSyncs()
            deleteAllRunsUseCase()
            logoutUseCase()
            clearSessionStorageUseCase()
        }
    }
}