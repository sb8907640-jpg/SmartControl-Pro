import Foundation

struct Device: Codable, Identifiable {
    let id: String
    let userId: String
    let deviceName: String?
    let deviceType: String?
    let osVersion: String?
    let appVariant: String?
    let status: String
    let fcmToken: String?
    let linkedAt: String?
    let unlinkedAt: String?
    let lastSeenAt: String?
    let createdAt: String?
    let activeConsents: Int?
    let features: [String]?
    let consents: [Consent]?

    enum CodingKeys: String, CodingKey {
        case id
        case userId = "user_id"
        case deviceName = "device_name"
        case deviceType = "device_type"
        case osVersion = "os_version"
        case appVariant = "app_variant"
        case status
        case fcmToken = "fcm_token"
        case linkedAt = "linked_at"
        case unlinkedAt = "unlinked_at"
        case lastSeenAt = "last_seen_at"
        case createdAt = "created_at"
        case activeConsents = "active_consents"
        case features
        case consents
    }

    var isOnline: Bool {
        guard let lastSeen = lastSeenAt?.toDate() else { return false }
        return Date().timeIntervalSince(lastSeen) < 5 * 60
    }

    var displayName: String {
        deviceName ?? "Unknown Device"
    }

    var displayType: String {
        deviceType?.capitalized ?? "Device"
    }
}

struct UnlinkRequest: Codable {
    let reason: String?
}