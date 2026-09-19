package com.smartcontrol.owner.data.repository

import com.smartcontrol.owner.data.api.ApiService
import com.smartcontrol.owner.data.model.Device
import com.smartcontrol.owner.data.model.UnlinkRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceRepository @Inject constructor(
    private val api: ApiService
) {

    suspend fun getDevices(): Result<List<Device>> {
        return try {
            val response = api.getDevices()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to load devices"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDevice(deviceId: String): Result<Device> {
        return try {
            val response = api.getDevice(deviceId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Device not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun unlinkDevice(deviceId: String, reason: String?): Result<Boolean> {
        return try {
            val response = api.unlinkDevice(deviceId, UnlinkRequest(reason))
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Failed to unlink device"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}