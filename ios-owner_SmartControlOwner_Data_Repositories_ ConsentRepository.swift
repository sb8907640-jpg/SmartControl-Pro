import Foundation

final class ConsentRepository {

    static let shared = ConsentRepository()
    private init() {}

    func getConsents(
        deviceId: String,
        completion: @escaping (Result<[Consent], APIError>) -> Void
    ) {
        APIClient.shared.request(.getConsents(deviceId), type: [Consent].self, completion: completion)
    }

    func grantConsent(
        deviceId: String,
        featureName: String,
        completion: @escaping (Result<Consent, APIError>) -> Void
    ) {
        let body = GrantConsentRequest(deviceId: deviceId, featureName: featureName)
        APIClient.shared.request(.grantConsent(body), type: Consent.self, completion: completion)
    }

    func revokeConsent(
        deviceId: String,
        featureName: String,
        completion: @escaping (Result<Consent, APIError>) -> Void
    ) {
        let body = RevokeConsentRequest(deviceId: deviceId, featureName: featureName)
        APIClient.shared.request(.revokeConsent(body), type: Consent.self, completion: completion)
    }

    func revokeAll(
        deviceId: String,
        completion: @escaping (Result<SuccessResponse, APIError>) -> Void
    ) {
        let body = RevokeAllRequest(deviceId: deviceId)
        APIClient.shared.request(.revokeAllConsents(body), type: SuccessResponse.self, completion: completion)
    }
}