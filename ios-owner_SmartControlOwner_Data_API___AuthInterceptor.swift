import Foundation
import Alamofire

final class AuthInterceptor: RequestInterceptor {

    func adapt(
        _ urlRequest: URLRequest,
        for session: Session,
        completion: @escaping (Result<URLRequest, Error>) -> Void
    ) {
        var request = urlRequest

        if let token = TokenManager.shared.getToken() {
            request.setValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
        }

        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        completion(.success(request))
    }

    func retry(
        _ request: Request,
        for session: Session,
        dueTo error: Error,
        completion: @escaping (RetryResult) -> Void
    ) {
        guard let response = request.task?.response as? HTTPURLResponse,
              response.statusCode == 401 else {
            completion(.doNotRetry)
            return
        }

        // Token expired — clear and force logout
        TokenManager.shared.clear()
        NotificationCenter.default.post(name: .sessionExpired, object: nil)
        completion(.doNotRetry)
    }
}

extension Notification.Name {
    static let sessionExpired = Notification.Name("sessionExpired")
}