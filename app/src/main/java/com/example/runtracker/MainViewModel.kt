package com.example.runtracker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.auth.SessionStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(
    private val sessionStorage: SessionStorage
) : ViewModel() {
    var state by mutableStateOf(MainState())
        private set

    init {
        viewModelScope.launch {
            state = state.copy(isCheckingAuth = true)
            state = state.copy(
                isLoggedIn = sessionStorage.get() != null
            )
            delay(SPLASH_SCREEN_DURATION)
            state = state.copy(isCheckingAuth = false)
        }
    }

    private companion object {
        const val SPLASH_SCREEN_DURATION = 500L
    }
}