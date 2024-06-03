package com.example.core.data.network

import com.example.core.data.BuildConfig

sealed class Endpoint(val url: String) {

    //TODO implement all necessary endpoint cases

    private companion object {
        const val BASE_URL = BuildConfig.BASE_URL
    }
}