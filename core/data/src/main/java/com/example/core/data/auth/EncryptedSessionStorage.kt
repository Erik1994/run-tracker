package com.example.core.data.auth

import android.content.SharedPreferences
import com.example.core.data.extension.get
import com.example.core.data.extension.putImmediately
import com.example.core.data.extension.removeImmediately
import com.example.core.domain.auth.AuthInfo
import com.example.core.domain.auth.SessionStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class EncryptedSessionStorage(
    private val sharedPreferences: SharedPreferences,
    private val ioDispatcher: CoroutineDispatcher
) : SessionStorage {
    override suspend fun get(): AuthInfo? {
        return withContext(ioDispatcher) {
            val json = sharedPreferences.get<String>(KEY_AUTH_INFO, null)
            json?.let {
                Json.decodeFromString<AuthInfoSerializable>(it).toAuthInfo()
            }
        }
    }

    override suspend fun set(info: AuthInfo?) {
        withContext(ioDispatcher) {
            if (info == null) {
                sharedPreferences.removeImmediately(KEY_AUTH_INFO)
                return@withContext
            }
            val json = Json.encodeToString(info.toAuthInfoSerializable())
            sharedPreferences.putImmediately<String>(KEY_AUTH_INFO, json)
        }
    }

    private companion object {
        const val KEY_AUTH_INFO = "KEY_AUTH_INFO"
    }
}