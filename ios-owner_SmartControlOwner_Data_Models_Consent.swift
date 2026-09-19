import Foundation

struct Consent: Codable, Identifiable {
    let id: String
    let deviceId: String
    let userId: String?
    let featureName: String
    let granted: Bool
    let grantedAt: String?
    let revokedAt: String?
    let ipAddress: String?
    let createdAt: String?

    enum CodingKeys: String, CodingKey {
        case id
        case deviceId = "device_id"
        case userId = "user_id"
        case featureName = "feature_name"
        case granted
        case grantedAt = "granted_at"
        case revokedAt = "revoked_at"
        case ipAddress = "ip_address"
        case createdAt = "created_at"
    }

    var isActive: Bool {
        granted && revokedAt == nil
    }
}

struct GrantConsentRequest: Codable {
    let deviceId: String
    let featureName: String

    enum CodingKeys: String, CodingKey {
        case deviceId = "deviceId"
        case featureName = "featureName"
    }
}

struct RevokeConsentRequest: Codable {
    let deviceId: String
    let featureName: String
}

struct RevokeAllRequest: Codable {
    let deviceId: String
}