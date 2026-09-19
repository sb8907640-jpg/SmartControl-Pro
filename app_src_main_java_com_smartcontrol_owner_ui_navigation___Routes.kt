package com.smartcontrol.owner.ui.navigation

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val DASHBOARD = "dashboard"
    const val ADD_DEVICE = "add_device"
    const val INVITE = "invite"
    const val DEVICE_DETAIL = "device_detail/{deviceId}"
    const val CONSENT_CENTER = "consent_center/{deviceId}"
    const val PROFILE = "profile"

    fun deviceDetail(deviceId: String) = "device_detail/$deviceId"
    fun consentCenter(deviceId: String) = "consent_center/$deviceId"
}