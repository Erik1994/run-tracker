package com.example.run.presentation.tracking

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class RunTrackingViewModel : ViewModel() {

    var state by mutableStateOf(RunTrackingState())
        private set

    private val eventChannel = Channel<RunTrackingEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: RunTrackingAction) {

    }
}