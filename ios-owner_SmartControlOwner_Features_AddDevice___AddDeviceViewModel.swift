import Foundation
import Combine

@MainActor
final class AddDeviceViewModel: ObservableObject {

    @Published var isLoading = false
    @Published var selectedDeviceType: String?
    @Published var selectedPermissions: Set<String> = Set(Constants.allPermissions.map { $0.id })
    @Published var selectedExpiryHours: Int = 24
    @Published var generatedInvite: InviteLink?
    @Published var error: String?

    func selectDeviceType(_ type: String?) {
        selectedDeviceType = type
    }

    func togglePermission(_ permissionId: String) {
        if selectedPermissions.contains(permissionId) {
            selectedPermissions.remove(permissionId)
        } else {
            selectedPermissions.insert(permissionId)
        }
    }

    func selectAllPermissions() {
        selectedPermissions = Set(Constants.allPermissions.map { $0.id })
    }

    func deselectAllPermissions() {
        selectedPermissions.removeAll()
    }

    func selectExpiry(hours: Int) {
        selectedExpiryHours = hours
    }

    func generateInvite(completion: @escaping (InviteLink) -> Void) {
        guard !selectedPermissions.isEmpty else {
            error = "Kam se kam ek permission select karein"
            return
        }

        isLoading = true
        error = nil

        LinkRepository.shared.generateInvite(
            deviceType: selectedDeviceType,
            permissions: Array(selectedPermissions),
            expiryHours: selectedExpiryHours
        ) { [weak self] result in
            DispatchQueue.main.async {
                guard let self = self else { return }
                self.isLoading = false

                switch result {
                case .success(let invite):
                    self.generatedInvite = invite
                    completion(invite)

                case .failure(let err):
                    self.error = err.localizedDescription
                }
            }
        }
    }

    func clearError() {
        error = nil
    }
}