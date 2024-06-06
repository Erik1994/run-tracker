package com.example.core.data.dispathcers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

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

object AppDispatchersImpl : AppDispatchers {
    override val ioDispatcher: CoroutineDispatcher by lazy { Dispatchers.IO }
    override val mainDispatcher: CoroutineDispatcher by lazy { Dispatchers.Main.immediate }
    override val defaultDispatcher: CoroutineDispatcher by lazy { Dispatchers.Default }
}