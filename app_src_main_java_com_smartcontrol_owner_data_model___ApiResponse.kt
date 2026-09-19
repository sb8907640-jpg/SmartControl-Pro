package com.smartcontrol.owner.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("success") val success: Boolean = true,
    @SerializedName("data") val data: T? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("error") val error: String? = null
)

data class ErrorResponse(
    @SerializedName("error") val error: String,
    @SerializedName("message") val message: String? = null,
    @SerializedName("feature") val feature: String? = null
)

data class SuccessResponse(
    @SerializedName("success") val success: Boolean = true,
    @SerializedName("message") val message: String? = null
)