import SwiftUI

struct AppTheme {

    // MARK: - Colors
    static let primary = Color(hex: "#2563EB")
    static let primaryDark = Color(hex: "#1E40AF")
    static let primaryLight = Color(hex: "#60A5FA")

    static let secondary = Color(hex: "#7C3AED")
    static let secondaryDark = Color(hex: "#5B21B6")

    static let success = Color(hex: "#10B981")
    static let warning = Color(hex: "#F59E0B")
    static let error = Color(hex: "#DC2626")
    static let errorDark = Color(hex: "#B91C1C")

    static let background = Color(hex: "#F9FAFB")
    static let surface = Color(hex: "#FFFFFF")
    static let surfaceVariant = Color(hex: "#F3F4F6")

    static let onPrimary = Color(hex: "#FFFFFF")
    static let onBackground = Color(hex: "#111827")
    static let onSurface = Color(hex: "#1F2937")
    static let onSurfaceVariant = Color(hex: "#6B7280")

    static let onlineGreen = Color(hex: "#10B981")
    static let offlineRed = Color(hex: "#EF4444")

    // MARK: - Spacing
    struct Spacing {
        static let xs: CGFloat = 4
        static let sm: CGFloat = 8
        static let md: CGFloat = 16
        static let lg: CGFloat = 24
        static let xl: CGFloat = 32
    }

    // MARK: - Corner Radius
    struct Radius {
        static let sm: CGFloat = 8
        static let md: CGFloat = 12
        static let lg: CGFloat = 16
        static let xl: CGFloat = 24
    }
}

// MARK: - Color Extension
extension Color {
    init(hex: String) {
        let hex = hex.trimmingCharacters(in: CharacterSet.alphanumerics.inverted)
        var int: UInt64 = 0
        Scanner(string: hex).scanHexInt64(&int)
        let a, r, g, b: UInt64
        switch hex.count {
        case 3:
            (a, r, g, b) = (255, (int >> 8) * 17, (int >> 4 & 0xF) * 17, (int & 0xF) * 17)
        case 6:
            (a, r, g, b) = (255, int >> 16, int >> 8 & 0xFF, int & 0xFF)
        case 8:
            (a, r, g, b) = (int >> 24, int >> 16 & 0xFF, int >> 8 & 0xFF, int & 0xFF)
        default:
            (a, r, g, b) = (255, 0, 0, 0)
        }
        self.init(
            .sRGB,
            red: Double(r) / 255,
            green: Double(g) / 255,
            blue: Double(b) / 255,
            opacity: Double(a) / 255
        )
    }
}