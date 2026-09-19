import Foundation

struct Constants {

    // MARK: - API
    static let baseURL = "https://api.smartcontrol.pro/api/"
    static let socketURL = "https://api.smartcontrol.pro"

    // MARK: - Keychain
    static let keychainService = "com.smartcontrol.owner"
    static let keychainToken = "auth_token"
    static let keychainUserId = "user_id"
    static let keychainUserEmail = "user_email"
    static let keychainUserName = "user_name"
    static let keychainUserRole = "user_role"

    // MARK: - Permissions (19 total)
    struct Permission: Identifiable, Hashable {
        let id: String
        let title: String
        let description: String
        let icon: String
    }

    static let allPermissions: [Permission] = [
        Permission(id: "location", title: "Location", description: "Live location share", icon: "📍"),
        Permission(id: "screen_lock", title: "Screen Lock Status", description: "Screen lock/unlock status", icon: "🔒"),
        Permission(id: "battery_network", title: "Battery & Network", description: "Battery and network info", icon: "🔋"),
        Permission(id: "camera", title: "Camera", description: "Camera access when you start", icon: "📷"),
        Permission(id: "mic", title: "Mic", description: "Microphone access when you start", icon: "🎤"),
        Permission(id: "app_usage", title: "App Usage", description: "Which apps are used", icon: "📱"),
        Permission(id: "sos", title: "SOS Alerts", description: "Emergency SOS alerts", icon: "🆘"),
        Permission(id: "touch_control", title: "Touch Control", description: "Touch control (consent-based)", icon: "👆"),
        Permission(id: "app_install", title: "App Install/Uninstall", description: "App install approval", icon: "📦"),
        Permission(id: "keyboard", title: "Keyboard Input", description: "Keyboard input (consent-based)", icon: "⌨️"),
        Permission(id: "screen_record", title: "Screen Recording", description: "Screen recording (consent)", icon: "🖥️"),
        Permission(id: "file_transfer", title: "File Transfer", description: "File transfer (consent)", icon: "📁"),
        Permission(id: "clipboard", title: "Clipboard Access", description: "Clipboard access (consent)", icon: "📋"),
        Permission(id: "clipboard_sync", title: "Clipboard Sync", description: "Clipboard sync (consent)", icon: "🔄"),
        Permission(id: "sms", title: "SMS (view only)", description: "SMS view (consent)", icon: "💬"),
        Permission(id: "call_logs", title: "Call Logs (view)", description: "Call logs view (consent)", icon: "📞"),
        Permission(id: "contacts", title: "Contacts (view)", description: "Contacts view (consent)", icon: "👥"),
        Permission(id: "gallery", title: "Gallery Access", description: "Gallery access (consent)", icon: "🖼️"),
        Permission(id: "notification_read", title: "Notification Read", description: "Notification read (consent)", icon: "🔔")
    ]

    // MARK: - Expiry Options
    struct ExpiryOption: Identifiable, Hashable {
        let id: Int
        let hours: Int
        let label: String
    }

    static let expiryOptions: [ExpiryOption] = [
        ExpiryOption(id: 24, hours: 24, label: "24 Hours"),
        ExpiryOption(id: 168, hours: 24 * 7, label: "7 Days"),
        ExpiryOption(id: 720, hours: 24 * 30, label: "30 Days")
    ]

    // MARK: - Device Types
    struct DeviceType: Identifiable, Hashable {
        let id: String
        let label: String
        let icon: String
    }

    static let deviceTypes: [DeviceType] = [
        DeviceType(id: "android", label: "Android Phone", icon: "📱"),
        DeviceType(id: "iphone", label: "iPhone", icon: "📱"),
        DeviceType(id: "tablet", label: "Tablet", icon: "📱"),
        DeviceType(id: "laptop", label: "Laptop", icon: "💻"),
        DeviceType(id: "desktop", label: "Desktop Computer", icon: "🖥️"),
        DeviceType(id: "web", label: "Web Browser", icon: "🌐")
    ]

    // MARK: - Socket Events
    struct SocketEvents {
        static let deviceLinked = "device:linked"
        static let deviceUnlinked = "device:unlinked"
        static let consentGranted = "consent:granted"
        static let consentRevoked = "consent:revoked"
        static let locationUpdate = "location:update"
        static let alert = "alert"
        static let stopAll = "stop:all"
    }
}