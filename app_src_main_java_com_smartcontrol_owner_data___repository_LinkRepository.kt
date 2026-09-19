package com.smartcontrol.owner.data.repository

import com.smartcontrol.owner.data.api.ApiService
import com.smartcontrol.owner.data.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LinkRepository @Inject constructor(
    private val api: ApiService
) {

    suspend fun generateInvite(
        deviceType: String?,
        permissions: List<String>,
        expiryHours: Int
    ): Result<InviteLink> {
        return try {
            val response = api.generateInvite(
                GenerateInviteRequest(deviceType, permissions, expiryHours)
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to generate invite"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun verifyInvite(code: String): Result<VerifyInviteResponse> {
        return try {
            val response = api.verifyInvite(code)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Invalid invite code"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getInviteLinks(): Result<List<InviteLink>> {
        return try {
            val response = api.getInviteLinks()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to load invites"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun cancelInvite(id: String): Result<Boolean> {
        return try {
            val response = api.cancelInvite(id)
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Failed to cancel invite"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}