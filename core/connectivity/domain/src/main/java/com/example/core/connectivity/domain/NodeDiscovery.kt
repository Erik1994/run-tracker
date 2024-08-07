package com.example.core.connectivity.domain

import kotlinx.coroutines.flow.Flow

interface NodeDiscovery {
    fun observeConnectedDevices(localDeviceType: DeviceType): Flow<Set<DeviceNode>>

    companion object {
        const val WATCH_REMOTE_CAPABILITY = "runners_wear_app"
        const val PHONE_REMOTE_CAPABILITY = "runners_phone_app"
    }
}