package com.example.auth.data.repository

import com.example.auth.data.request.LoginRequest
import com.example.auth.data.request.RegisterRequest
import com.example.auth.domain.repository.AuthRepository
import com.example.auth.data.response.LoginResponse
import com.example.core.data.network.Endpoint
import com.example.core.data.network.post
import com.example.core.domain.auth.AuthInfo
import com.example.core.domain.auth.SessionStorage
import com.example.core.domain.util.DataError
import com.example.core.domain.util.EmptyResult
import com.example.core.domain.util.Result
import com.example.core.domain.util.asEmptyDataResult
import io.ktor.client.HttpClient

class AuthRepositoryImpl(
   private val httpClient: HttpClient,
    private val sessionStorage: SessionStorage
): AuthRepository {

    override suspend fun login(email: String, password: String): EmptyResult<DataError.Network> {
        val result = httpClient.post<LoginRequest, LoginResponse>(
            endpoint = Endpoint.Login,
            body = LoginRequest(
                email = email,
                password = password
            )
        )
        if (result is Result.Success) {
            sessionStorage.set(
                AuthInfo(
                    accessToken = result.data.accessToken,
                    refreshToken = result.data.refreshToken,
                    userId = result.data.userId
                )
            )
        }
        return result.asEmptyDataResult()
    }

    override suspend fun register(email: String, password: String): EmptyResult<DataError.Network> {
        return httpClient.post<RegisterRequest, Unit>(
            Endpoint.Register,
            body = RegisterRequest(
                email = email,
                password = password
            )
        )
    }
}