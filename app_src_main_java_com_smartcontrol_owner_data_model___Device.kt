package com.smartcontrol.owner.data.model

import com.google.gson.annotations.SerializedName

data class Device(
    @SerializedName("id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("device_name") val deviceName: String?,
    @SerializedName("device_type") val deviceType: String?,
    @SerializedName("os_version") val osVersion: String?,
    @SerializedName("app_variant") val appVariant: String?,
    @SerializedName("status") val status: String,
    @SerializedName("fcm_token") val fcmToken: String?,
    @SerializedName("linked_at") val linkedAt: String?,
    @SerializedName("unlinked_at") val unlinkedAt: String?,
    @SerializedName("last_seen_at") val lastSeenAt: String?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("active_consents") val activeConsents: Int = 0,
    @SerializedName("features") val features: List<String>? = null,
    @SerializedName("consents") val consents: List<Consent>? = null
) {
    val isOnline: Boolean
        get() = lastSeenAt != null && isWithinLastMinutes(5)

    private fun isWithinLastMinutes(minutes: Int): Boolean {
        return try {
            val lastSeen = com.smartcontrol.owner.util.DateTimeUtil.parseIso(lastSeenAt)
            val diff = System.currentTimeMillis() - (lastSeen?.time ?: 0)
            diff < minutes * 60 * 1000
        } catch (e: Exception) {
            false
        }
    }
}

data class UnlinkRequest(
    @SerializedName("reason") val reason: String?
)