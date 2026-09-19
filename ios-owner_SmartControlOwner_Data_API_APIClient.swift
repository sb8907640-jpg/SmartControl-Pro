import Foundation
import Alamofire

enum APIError: Error, LocalizedError {
    case invalidURL
    case noData
    case decodingError(Error)
    case serverError(Int, String?)
    case unauthorized
    case networkError(Error)

    var errorDescription: String? {
        switch self {
        case .invalidURL: return "Invalid URL"
        case .noData: return "No data received"
        case .decodingError(let error): return "Decoding failed: \(error.localizedDescription)"
        case .serverError(let code, let msg): return msg ?? "Server error \(code)"
        case .unauthorized: return "Session expired. Please login again."
        case .networkError(let error): return error.localizedDescription
        }
    }
}

final class APIClient {

    static let shared = APIClient()
    private init() {}

    private let session: Session = {
        let configuration = URLSessionConfiguration.default
        configuration.timeoutIntervalForRequest = 30
        configuration.timeoutIntervalForResource = 60
        configuration.httpAdditionalHeaders = [
            "Accept": "application/json",
            "X-Client": "ios-owner",
            "X-App-Version": "6.0.0"
        ]

        let interceptor = AuthInterceptor()
        return Session(configuration: configuration, interceptor: interceptor)
    }()

    func request<T: Decodable>(
        _ router: APIRouter,
        type: T.Type,
        completion: @escaping (Result<T, APIError>) -> Void
    ) {
        session.request(router)
            .validate()
            .responseDecodable(of: T.self) { response in
                switch response.result {
                case .success(let value):
                    completion(.success(value))

                case .failure(let error):
                    if let statusCode = response.response?.statusCode {
                        if statusCode == 401 {
                            completion(.failure(.unauthorized))
                            return
                        }

                        if let data = response.data,
                           let errorResponse = try? JSONDecoder().decode(ErrorResponse.self, from: data) {
                            completion(.failure(.serverError(statusCode, errorResponse.error)))
                            return
                        }
                        completion(.failure(.serverError(statusCode, error.localizedDescription)))
                    } else if let afError = error as? AFError,
                              case .responseSerializationFailed(let reason) = afError,
                              case .decodingFailed(let decodingError) = reason {
                        completion(.failure(.decodingError(decodingError)))
                    } else {
                        completion(.failure(.networkError(error)))
                    }
                }
            }
    }
}