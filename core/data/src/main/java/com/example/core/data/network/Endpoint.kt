package com.example.core.data.network

import com.example.core.data.BuildConfig

sealed class Endpoint(val url: String) {

    data object Register: Endpoint("$BASE_URL/register")

    private companion object {
        const val BASE_URL = BuildConfig.BASE_URL
    }
}