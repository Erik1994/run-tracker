package com.example.run.presentation.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RunOverviewViewModel: ViewModel() {

    private val channelEvents = Channel<RunOverviewEvent>()
    val events = channelEvents.receiveAsFlow()

    fun onAction(action: RunOverviewAction) {
        when(action) {
            RunOverviewAction.OnAnalyticsClick -> {}
            RunOverviewAction.OnLogoutClick -> {}
            RunOverviewAction.OnStartClick -> navigateToRunTracking()
        }
    }

    private fun navigateToRunTracking() {
        viewModelScope.launch {
            channelEvents.send(RunOverviewEvent.RunTrackingNavigation)
        }
    }
}