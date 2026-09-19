import Foundation

struct ApiResponse<T: Codable>: Codable {
    let success: Bool?
    let data: T?
    let message: String?
    let error: String?
}

struct ErrorResponse: Codable {
    let error: String
    let message: String?
    let feature: String?
}

struct SuccessResponse: Codable {
    let success: Bool?
    let message: String?
}