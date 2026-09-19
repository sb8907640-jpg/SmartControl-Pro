import Foundation
import Combine

@MainActor
final class AuthViewModel: ObservableObject {

    @Published var isLoading = false
    @Published var isLoggedIn = false
    @Published var error: String?
    @Published var userName: String?

    // MARK: - Login
    func login(email: String, password: String) {
        guard !email.isEmpty, !password.isEmpty else {
            error = "Email and password required"
            return
        }

        guard email.isValidEmail else {
            error = "Please enter a valid email"
            return
        }

        isLoading = true
        error = nil

        AuthRepository.shared.login(email: email, password: password) { [weak self] result in
            DispatchQueue.main.async {
                guard let self = self else { return }
                self.isLoading = false

                switch result {
                case .success(let auth):
                    self.isLoggedIn = true
                    self.userName = auth.user.name

                case .failure(let err):
                    self.error = err.localizedDescription
                }
            }
        }
    }

    // MARK: - Register
    func register(email: String, password: String, name: String?, mobile: String?) {
        guard !email.isEmpty, !password.isEmpty else {
            error = "Email and password required"
            return
        }

        guard email.isValidEmail else {
            error = "Please enter a valid email"
            return
        }

        guard password.count >= 6 else {
            error = "Password must be at least 6 characters"
            return
        }

        isLoading = true
        error = nil

        AuthRepository.shared.register(
            email: email,
            password: password,
            name: name,
            mobile: mobile
        ) { [weak self] result in
            DispatchQueue.main.async {
                guard let self = self else { return }
                self.isLoading = false

                switch result {
                case .success(let auth):
                    self.isLoggedIn = true
                    self.userName = auth.user.name

                case .failure(let err):
                    self.error = err.localizedDescription
                }
            }
        }
    }

    func clearError() {
        error = nil
    }

    func logout() {
        AuthRepository.shared.logout()
        isLoggedIn = false
    }
}