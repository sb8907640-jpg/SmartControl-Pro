import Foundation

final class DeviceRepository {

    static let shared = DeviceRepository()
    private init() {}

    func getDevices(completion: @escaping (Result<[Device], APIError>) -> Void) {
        APIClient.shared.request(.getDevices, type: [Device].self, completion: completion)
    }

    func getDevice(id: String, completion: @escaping (Result<Device, APIError>) -> Void) {
        APIClient.shared.request(.getDevice(id), type: Device.self, completion: completion)
    }

    func unlinkDevice(
        id: String,
        reason: String?,
        completion: @escaping (Result<SuccessResponse, APIError>) -> Void
    ) {
        let body = UnlinkRequest(reason: reason)
        APIClient.shared.request(.unlinkDevice(id, body), type: SuccessResponse.self, completion: completion)
    }
}