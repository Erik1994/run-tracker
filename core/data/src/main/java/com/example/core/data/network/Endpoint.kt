package com.example.core.data.network

import com.example.core.data.BuildConfig

sealed class Endpoint(val url: String) {

    data object Login : Endpoint("$BASE_URL/login")
    data object Register: Endpoint("$BASE_URL/register")

    data object AccessToken: Endpoint("$BASE_URL/accessToken")

    private companion object {
        const val BASE_URL = BuildConfig.BASE_URL
    }
}