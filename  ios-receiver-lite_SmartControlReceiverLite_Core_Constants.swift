import Foundation

struct ReceiverConstants {

    static let apiBaseURL = "https://api.smartcontrol.pro/api/"
    static let keychainService = "com.smartcontrol.receiverlite"

    // Permissions shown to receiver (same as owner)
    static let allPermissions: [ReceiverPermission] = [
        ReceiverPermission(id: "location", title: "Location", description: "Live location share", icon: "📍"),
        ReceiverPermission(id: "screen_lock", title: "Screen Lock Status", description: "Screen lock/unlock status", icon: "🔒"),
        ReceiverPermission(id: "battery_network", title: "Battery & Network", description: "Battery and network info", icon: "🔋"),
        ReceiverPermission(id: "camera", title: "Camera", description: "Camera access when you start", icon: "📷"),
        ReceiverPermission(id: "mic", title: "Mic", description: "Microphone access when you start", icon: "🎤"),
        ReceiverPermission(id: "app_usage", title: "App Usage", description: "Which apps are used", icon: "📱"),
        ReceiverPermission(id: "sos", title: "SOS Alerts", description: "Emergency SOS alerts", icon: "🆘"),
        ReceiverPermission(id: "touch_control", title: "Touch Control", description: "Touch control (consent-based)", icon: "👆"),
        ReceiverPermission(id: "app_install", title: "App Install/Uninstall", description: "App install approval", icon: "📦"),
        ReceiverPermission(id: "keyboard", title: "Keyboard Input", description: "Keyboard input (consent-based)", icon: "⌨️"),
        ReceiverPermission(id: "screen_record", title: "Screen Recording", description: "Screen recording (consent)", icon: "🖥️"),
        ReceiverPermission(id: "file_transfer", title: "File Transfer", description: "File transfer (consent)", icon: "📁"),
        ReceiverPermission(id: "clipboard", title: "Clipboard Access", description: "Clipboard access (consent)", icon: "📋"),
        ReceiverPermission(id: "clipboard_sync", title: "Clipboard Sync", description: "Clipboard sync (consent)", icon: "🔄"),
        ReceiverPermission(id: "sms", title: "SMS (view only)", description: "SMS view (consent)", icon: "💬"),
        ReceiverPermission(id: "call_logs", title: "Call Logs (view)", description: "Call logs view (consent)", icon: "📞"),
        ReceiverPermission(id: "contacts", title: "Contacts (view)", description: "Contacts view (consent)", icon: "👥"),
        ReceiverPermission(id: "gallery", title: "Gallery Access", description: "Gallery access (consent)", icon: "🖼️"),
        ReceiverPermission(id: "notification_read", title: "Notification Read", description: "Notification read (consent)", icon: "🔔")
    ]
}

struct ReceiverPermission: Identifiable, Hashable {
    let id: String
    let title: String
    let description: String
    let icon: String
}