import Foundation

final class LinkRepository {

    static let shared = LinkRepository()
    private init() {}

    func generateInvite(
        deviceType: String?,
        permissions: [String],
        expiryHours: Int,
        completion: @escaping (Result<InviteLink, APIError>) -> Void
    ) {
        let body = GenerateInviteRequest(
            deviceType: deviceType,
            permissions: permissions,
            expiryHours: expiryHours
        )
        APIClient.shared.request(.generateInvite(body), type: InviteLink.self, completion: completion)
    }

    func verifyInvite(
        code: String,
        completion: @escaping (Result<VerifyInviteResponse, APIError>) -> Void
    ) {
        APIClient.shared.request(.verifyInvite(code), type: VerifyInviteResponse.self, completion: completion)
    }

    func getInviteLinks(completion: @escaping (Result<[InviteLink], APIError>) -> Void) {
        APIClient.shared.request(.getInviteLinks, type: [InviteLink].self, completion: completion)
    }

    func cancelInvite(id: String, completion: @escaping (Result<SuccessResponse, APIError>) -> Void) {
        APIClient.shared.request(.cancelInvite(id), type: SuccessResponse.self, completion: completion)
    }
}