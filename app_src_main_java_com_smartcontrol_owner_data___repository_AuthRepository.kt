package com.smartcontrol.owner.data.repository

import com.smartcontrol.owner.data.api.ApiService
import com.smartcontrol.owner.data.local.TokenManager
import com.smartcontrol.owner.data.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: ApiService,
    private val tokenManager: TokenManager
) {

    suspend fun register(email: String, password: String, name: String?, mobile: String?): Result<AuthResponse> {
        return try {
            val response = api.register(RegisterRequest(email, password, name, mobile))
            if (response.isSuccessful && response.body() != null) {
                val auth = response.body()!!
                tokenManager.saveAuth(
                    token = auth.token,
                    userId = auth.user.id,
                    email = auth.user.email,
                    name = auth.user.name,
                    role = auth.user.role
                )
                Result.success(auth)
            } else {
                Result.failure(Exception(parseError(response.errorBody()?.string())))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                val auth = response.body()!!
                tokenManager.saveAuth(
                    token = auth.token,
                    userId = auth.user.id,
                    email = auth.user.email,
                    name = auth.user.name,
                    role = auth.user.role
                )
                Result.success(auth)
            } else {
                Result.failure(Exception(parseError(response.errorBody()?.string())))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        tokenManager.clear()
    }

    suspend fun isLoggedIn(): Boolean = tokenManager.isLoggedIn()

    private fun parseError(body: String?): String {
        return try {
            body ?: "Unknown error"
        } catch (e: Exception) {
            "Unknown error"
        }
    }
}