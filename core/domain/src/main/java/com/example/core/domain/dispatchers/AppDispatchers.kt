package com.example.core.domain.dispatchers

import kotlinx.coroutines.CoroutineDispatcher

interface AppDispatchers {
    val ioDispatcher: CoroutineDispatcher
    val mainDispatcher: CoroutineDispatcher
    val defaultDispatcher: CoroutineDispatcher

    companion object {
        const val DISPATCHER_IO = "DISPATCHER_IO"
        const val DISPATCHER_UI = "DISPATCHER_UI"
        const val DISPATCHER_DEFAULT = "DISPATCHER_DEFAULT"
    }
}