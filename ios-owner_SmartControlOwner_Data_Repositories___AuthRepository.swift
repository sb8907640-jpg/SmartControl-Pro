import Foundation

final class AuthRepository {

    static let shared = AuthRepository()
    private init() {}

    func register(
        email: String,
        password: String,
        name: String?,
        mobile: String?,
        completion: @escaping (Result<AuthResponse, APIError>) -> Void
    ) {
        let request = RegisterRequest(
            email: email,
            password: password,
            name: name,
            mobile: mobile
        )

        APIClient.shared.request(.register(request), type: AuthResponse.self) { result in
            switch result {
            case .success(let auth):
                TokenManager.shared.saveAuth(
                    token: auth.token,
                    userId: auth.user.id,
                    email: auth.user.email,
                    name: auth.user.name,
                    role: auth.user.role
                )
                completion(.success(auth))
            case .failure(let error):
                completion(.failure(error))
            }
        }
    }

    func login(
        email: String,
        password: String,
        completion: @escaping (Result<AuthResponse, APIError>) -> Void
    ) {
        let request = LoginRequest(email: email, password: password)

        APIClient.shared.request(.login(request), type: AuthResponse.self) { result in
            switch result {
            case .success(let auth):
                TokenManager.shared.saveAuth(
                    token: auth.token,
                    userId: auth.user.id,
                    email: auth.user.email,
                    name: auth.user.name,
                    role: auth.user.role
                )
                completion(.success(auth))
            case .failure(let error):
                completion(.failure(error))
            }
        }
    }

    func logout() {
        TokenManager.shared.clear()
    }

    var isLoggedIn: Bool {
        TokenManager.shared.isLoggedIn
    }
}