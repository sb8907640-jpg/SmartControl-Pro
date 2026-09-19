package com.smartcontrol.owner.util

object Constants {

    // API
    const val BASE_URL = "https://api.smartcontrol.pro/api/"
    const val SOCKET_URL = "https://api.smartcontrol.pro"

    // DataStore
    const val PREF_NAME = "smartcontrol_prefs"
    const val KEY_TOKEN = "auth_token"
    const val KEY_USER_ID = "user_id"
    const val KEY_USER_EMAIL = "user_email"
    const val KEY_USER_NAME = "user_name"
    const val KEY_USER_ROLE = "user_role"

    // Permissions (19 total)
    val ALL_PERMISSIONS = listOf(
        Permission("location", "Location", "Live location share", "📍"),
        Permission("screen_lock", "Screen Lock Status", "Screen lock/unlock status", "🔒"),
        Permission("battery_network", "Battery & Network", "Battery and network info", "🔋"),
        Permission("camera", "Camera", "Camera access when you start", "📷"),
        Permission("mic", "Mic", "Microphone access when you start", "🎤"),
        Permission("app_usage", "App Usage", "Which apps are used", "📱"),
        Permission("sos", "SOS Alerts", "Emergency SOS alerts", "🆘"),
        Permission("touch_control", "Touch Control", "Touch control (consent-based)", "👆"),
        Permission("app_install", "App Install/Uninstall", "App install approval", "📦"),
        Permission("keyboard", "Keyboard Input", "Keyboard input (consent-based)", "⌨️"),
        Permission("screen_record", "Screen Recording", "Screen recording (consent)", "🖥️"),
        Permission("file_transfer", "File Transfer", "File transfer (consent)", "📁"),
        Permission("clipboard", "Clipboard Access", "Clipboard access (consent)", "📋"),
        Permission("clipboard_sync", "Clipboard Sync", "Clipboard sync (consent)", "🔄"),
        Permission("sms", "SMS (view only)", "SMS view (consent)", "💬"),
        Permission("call_logs", "Call Logs (view)", "Call logs view (consent)", "📞"),
        Permission("contacts", "Contacts (view)", "Contacts view (consent)", "👥"),
        Permission("gallery", "Gallery Access", "Gallery access (consent)", "🖼️"),
        Permission("notification_read", "Notification Read", "Notification read (consent)", "🔔")
    )

    // Link expiry options (hours)
    val EXPIRY_OPTIONS = listOf(
        ExpiryOption(24, "24 Hours"),
        ExpiryOption(24 * 7, "7 Days"),
        ExpiryOption(24 * 30, "30 Days")
    )

    // Device types
    val DEVICE_TYPES = listOf(
        DeviceType("android", "Android Phone", "📱"),
        DeviceType("iphone", "iPhone", "📱"),
        DeviceType("tablet", "Tablet", "📱"),
        DeviceType("laptop", "Laptop", "💻"),
        DeviceType("desktop", "Desktop Computer", "🖥️"),
        DeviceType("web", "Web Browser", "🌐")
    )

    // Socket events
    const val SOCKET_EVENT_DEVICE_LINKED = "device:linked"
    const val SOCKET_EVENT_DEVICE_UNLINKED = "device:unlinked"
    const val SOCKET_EVENT_CONSENT_GRANTED = "consent:granted"
    const val SOCKET_EVENT_CONSENT_REVOKED = "consent:revoked"
    const val SOCKET_EVENT_LOCATION_UPDATE = "location:update"
    const val SOCKET_EVENT_ALERT = "alert"
    const val SOCKET_EVENT_STOP_ALL = "stop:all"
}

data class Permission(
    val id: String,
    val title: String,
    val description: String,
    val icon: String
)

data class ExpiryOption(
    val hours: Int,
    val label: String
)

data class DeviceType(
    val id: String,
    val label: String,
    val icon: String
)