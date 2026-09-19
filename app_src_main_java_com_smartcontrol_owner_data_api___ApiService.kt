package com.smartcontrol.owner.data.api

import com.smartcontrol.owner.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ============================================
    // AUTH
    // ============================================
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    // ============================================
    // DEVICES
    // ============================================
    @GET("devices")
    suspend fun getDevices(): Response<List<Device>>

    @GET("devices/{deviceId}")
    suspend fun getDevice(@Path("deviceId") deviceId: String): Response<Device>

    @DELETE("devices/{deviceId}")
    suspend fun unlinkDevice(
        @Path("deviceId") deviceId: String,
        @Body request: UnlinkRequest
    ): Response<SuccessResponse>

    // ============================================
    // CONSENT
    // ============================================
    @GET("consent/{deviceId}")
    suspend fun getConsents(@Path("deviceId") deviceId: String): Response<List<Consent>>

    @POST("consent/grant")
    suspend fun grantConsent(@Body request: GrantConsentRequest): Response<Consent>

    @POST("consent/revoke")
    suspend fun revokeConsent(@Body request: RevokeConsentRequest): Response<Consent>

    @POST("consent/revoke-all")
    suspend fun revokeAllConsents(@Body request: RevokeAllRequest): Response<SuccessResponse>

    // ============================================
    // INVITE LINKS
    // ============================================
    @POST("link/generate")
    suspend fun generateInvite(@Body request: GenerateInviteRequest): Response<InviteLink>

    @GET("link/verify/{code}")
    suspend fun verifyInvite(@Path("code") code: String): Response<VerifyInviteResponse>

    @GET("link")
    suspend fun getInviteLinks(): Response<List<InviteLink>>

    @DELETE("link/{id}")
    suspend fun cancelInvite(@Path("id") id: String): Response<SuccessResponse>

    // ============================================
    // LOCATION
    // ============================================
    @GET("location/{deviceId}")
    suspend fun getLocationHistory(
        @Path("deviceId") deviceId: String,
        @Query("limit") limit: Int = 100
    ): Response<List<Map<String, Any>>>
}