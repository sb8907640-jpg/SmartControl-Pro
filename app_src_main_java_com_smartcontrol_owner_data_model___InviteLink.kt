package com.smartcontrol.owner.data.model

import com.google.gson.annotations.SerializedName

data class InviteLink(
    @SerializedName("id") val id: String,
    @SerializedName("code") val code: String,
    @SerializedName("url") val url: String,
    @SerializedName("shortCode") val shortCode: String,
    @SerializedName("expiresAt") val expiresAt: String,
    @SerializedName("deviceType") val deviceType: String?,
    @SerializedName("permissions") val permissions: List<String>,
    @SerializedName("status") val status: String? = null,
    @SerializedName("used_at") val usedAt: String? = null,
    @SerializedName("created_at") val createdAt: String? = null
)

data class GenerateInviteRequest(
    @SerializedName("deviceType") val deviceType: String?,
    @SerializedName("permissions") val permissions: List<String>,
    @SerializedName("expiryHours") val expiryHours: Int = 24
)

data class VerifyInviteResponse(
    @SerializedName("valid") val valid: Boolean,
    @SerializedName("ownerName") val ownerName: String?,
    @SerializedName("ownerEmail") val ownerEmail: String?,
    @SerializedName("deviceType") val deviceType: String?,
    @SerializedName("permissions") val permissions: List<String>,
    @SerializedName("expiresAt") val expiresAt: String
)