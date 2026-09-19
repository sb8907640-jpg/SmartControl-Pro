package com.smartcontrol.owner.data.model

import com.google.gson.annotations.SerializedName

data class Consent(
    @SerializedName("id") val id: String,
    @SerializedName("device_id") val deviceId: String,
    @SerializedName("user_id") val userId: String?,
    @SerializedName("feature_name") val featureName: String,
    @SerializedName("granted") val granted: Boolean,
    @SerializedName("granted_at") val grantedAt: String?,
    @SerializedName("revoked_at") val revokedAt: String?,
    @SerializedName("ip_address") val ipAddress: String?,
    @SerializedName("metadata") val metadata: Map<String, Any>? = null,
    @SerializedName("created_at") val createdAt: String? = null
)

data class GrantConsentRequest(
    @SerializedName("deviceId") val deviceId: String,
    @SerializedName("featureName") val featureName: String,
    @SerializedName("metadata") val metadata: Map<String, Any>? = null
)

data class RevokeConsentRequest(
    @SerializedName("deviceId") val deviceId: String,
    @SerializedName("featureName") val featureName: String
)

data class RevokeAllRequest(
    @SerializedName("deviceId") val deviceId: String
)