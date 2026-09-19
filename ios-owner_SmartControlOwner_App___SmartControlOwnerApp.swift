import SwiftUI
import FirebaseCore
import FirebaseMessaging

@main
struct SmartControlOwnerApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    var body: some Scene {
        WindowGroup {
            AppNavigation()
                .preferredColorScheme(.light)
        }
    }
}