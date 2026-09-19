import Foundation

struct InviteLink: Codable, Identifiable {
    let id: String
    let code: String
    let url: String
    let shortCode: String
    let expiresAt: String
    let deviceType: String?
    let permissions: [String]
    let status: String?
    let usedAt: String?
    let createdAt: String?
}

struct GenerateInviteRequest: Codable {
    let deviceType: String?
    let permissions: [String]
    let expiryHours: Int
}

struct VerifyInviteResponse: Codable {
    let valid: Bool
    let ownerName: String?
    let ownerEmail: String?
    let deviceType: String?
    let permissions: [String]
    let expiresAt: String
}