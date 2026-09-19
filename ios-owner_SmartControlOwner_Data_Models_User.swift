import Foundation

struct User: Codable, Identifiable {
    let id: String
    let email: String
    let name: String?
    let mobile: String?
    let role: String
    let emailVerified: Bool?
    let createdAt: String?

    enum CodingKeys: String, CodingKey {
        case id
        case email
        case name
        case mobile
        case role
        case emailVerified = "email_verified"
        case createdAt = "created_at"
    }
}

struct LoginRequest: Codable {
    let email: String
    let password: String
}

struct RegisterRequest: Codable {
    let email: String
    let password: String
    let name: String?
    let mobile: String?
}

struct AuthResponse: Codable {
    let user: User
    let token: String
}