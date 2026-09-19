package com.smartcontrol.owner.data.repository

import com.smartcontrol.owner.data.api.ApiService
import com.smartcontrol.owner.data.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConsentRepository @Inject constructor(
    private val api: ApiService
) {

    suspend fun getConsents(deviceId: String): Result<List<Consent>> {
        return try {
            val response = api.getConsents(deviceId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to load consents"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun grantConsent(deviceId: String, featureName: String): Result<Consent> {
        return try {
            val response = api.grantConsent(GrantConsentRequest(deviceId, featureName))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to grant consent"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun revokeConsent(deviceId: String, featureName: String): Result<Consent> {
        return try {
            val response = api.revokeConsent(RevokeConsentRequest(deviceId, featureName))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to revoke consent"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun revokeAll(deviceId: String): Result<Boolean> {
        return try {
            val response = api.revokeAllConsents(RevokeAllRequest(deviceId))
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Failed to revoke all"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}