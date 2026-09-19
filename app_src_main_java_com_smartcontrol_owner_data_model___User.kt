package com.smartcontrol.owner.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: String,
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String?,
    @SerializedName("mobile") val mobile: String?,
    @SerializedName("role") val role: String = "user",
    @SerializedName("email_verified") val emailVerified: Boolean = false,
    @SerializedName("created_at") val createdAt: String? = null
)

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class RegisterRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("name") val name: String?,
    @SerializedName("mobile") val mobile: String?
)

data class AuthResponse(
    @SerializedName("user") val user: User,
    @SerializedName("token") val token: String
)