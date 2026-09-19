import Foundation
import Alamofire

enum APIRouter: URLRequestConvertible {

    // Auth
    case register(RegisterRequest)
    case login(LoginRequest)

    // Devices
    case getDevices
    case getDevice(String)
    case unlinkDevice(String, UnlinkRequest)

    // Consent
    case getConsents(String)
    case grantConsent(GrantConsentRequest)
    case revokeConsent(RevokeConsentRequest)
    case revokeAllConsents(RevokeAllRequest)

    // Invites
    case generateInvite(GenerateInviteRequest)
    case verifyInvite(String)
    case getInviteLinks
    case cancelInvite(String)

    // Location
    case getLocationHistory(String, Int)

    var method: HTTPMethod {
        switch self {
        case .register, .login,
             .grantConsent, .revokeConsent, .revokeAllConsents,
             .generateInvite:
            return .post

        case .getDevices, .getDevice, .getConsents,
             .verifyInvite, .getInviteLinks, .getLocationHistory:
            return .get

        case .unlinkDevice, .cancelInvite:
            return .delete
        }
    }

    var path: String {
        switch self {
        case .register: return "auth/register"
        case .login: return "auth/login"
        case .getDevices: return "devices"
        case .getDevice(let id): return "devices/\(id)"
        case .unlinkDevice(let id, _): return "devices/\(id)"
        case .getConsents(let deviceId): return "consent/\(deviceId)"
        case .grantConsent: return "consent/grant"
        case .revokeConsent: return "consent/revoke"
        case .revokeAllConsents: return "consent/revoke-all"
        case .generateInvite: return "link/generate"
        case .verifyInvite(let code): return "link/verify/\(code)"
        case .getInviteLinks: return "link"
        case .cancelInvite(let id): return "link/\(id)"
        case .getLocationHistory(let deviceId, _): return "location/\(deviceId)"
        }
    }

    var parameters: Parameters? {
        switch self {
        case .register(let body):
            return body.dictionary
        case .login(let body):
            return body.dictionary
        case .unlinkDevice(_, let body):
            return body.dictionary
        case .grantConsent(let body):
            return body.dictionary
        case .revokeConsent(let body):
            return body.dictionary
        case .revokeAllConsents(let body):
            return body.dictionary
        case .generateInvite(let body):
            return body.dictionary
        case .getLocationHistory(_, let limit):
            return ["limit": limit]
        default:
            return nil
        }
    }

    func asURLRequest() throws -> URLRequest {
        let url = try Constants.baseURL.asURL()
        var request = URLRequest(url: url.appendingPathComponent(path))
        request.httpMethod = method.rawValue
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")

        if let parameters = parameters {
            return try JSONEncoding.default.encode(request, with: parameters)
        }

        return request
    }
}

// MARK: - Codable to Dictionary helpers
extension Encodable {
    var dictionary: [String: Any]? {
        guard let data = try? JSONEncoder().encode(self) else { return nil }
        return (try? JSONSerialization.jsonObject(with: data)) as? [String: Any]
    }
}