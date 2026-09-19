import Foundation
import KeychainAccess

final class TokenManager {

    static let shared = TokenManager()
    private init() {}

    private let keychain = Keychain(service: Constants.keychainService)
        .accessibility(.afterFirstUnlock)

    // MARK: - Save
    func saveAuth(token: String, userId: String, email: String, name: String?, role: String) {
        try? keychain.set(token, key: Constants.keychainToken)
        try? keychain.set(userId, key: Constants.keychainUserId)
        try? keychain.set(email, key: Constants.keychainUserEmail)
        try? keychain.set(name ?? "", key: Constants.keychainUserName)
        try? keychain.set(role, key: Constants.keychainUserRole)
    }

    // MARK: - Read
    func getToken() -> String? {
        try? keychain.get(Constants.keychainToken)
    }

    func getUserId() -> String? {
        try? keychain.get(Constants.keychainUserId)
    }

    func getUserEmail() -> String? {
        try? keychain.get(Constants.keychainUserEmail)
    }

    func getUserName() -> String? {
        try? keychain.get(Constants.keychainUserName)
    }

    func getUserRole() -> String? {
        try? keychain.get(Constants.keychainUserRole)
    }

    // MARK: - Check
    var isLoggedIn: Bool {
        getToken()?.isEmpty == false
    }

    // MARK: - Clear
    func clear() {
        try? keychain.removeAll()
    }
}