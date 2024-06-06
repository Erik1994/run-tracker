package com.example.auth.data.repository

import com.example.auth.data.request.RegisterRequest
import com.example.auth.domain.repository.AuthRepository
import com.example.core.data.network.Endpoint
import com.example.core.data.network.post
import com.example.core.domain.util.DataError
import com.example.core.domain.util.EmptyResult
import io.ktor.client.HttpClient

class AuthRepositoryImpl(
   private val httpClient: HttpClient
): AuthRepository {
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