import Foundation
import SwiftUI

extension Date {
    func toDisplayString() -> String {
        let formatter = DateFormatter()
        formatter.dateFormat = "dd MMM yyyy, hh:mm a"
        return formatter.string(from: self)
    }

    func toDateOnlyString() -> String {
        let formatter = DateFormatter()
        formatter.dateFormat = "dd MMM yyyy"
        return formatter.string(from: self)
    }

    func timeAgo() -> String {
        let now = Date()
        let diff = now.timeIntervalSince(self)

        let seconds = Int(diff)
        let minutes = seconds / 60
        let hours = minutes / 60
        let days = hours / 24

        if seconds < 60 { return "Just now" }
        if minutes < 60 { return "\(minutes) min ago" }
        if hours < 24 { return "\(hours) hr ago" }
        if days < 7 { return "\(days) days ago" }
        return self.toDateOnlyString()
    }
}

extension String {
    func toDate() -> Date? {
        let formatter = ISO8601DateFormatter()
        formatter.formatOptions = [.withInternetDateTime, .withFractionalSeconds]
        if let date = formatter.date(from: self) { return date }

        let fallback = DateFormatter()
        fallback.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        fallback.timeZone = TimeZone(identifier: "UTC")
        return fallback.date(from: self)
    }

    var isValidEmail: Bool {
        let regex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
        return NSPredicate(format: "SELF MATCHES %@", regex).evaluate(with: self)
    }
}

extension View {
    func cardStyle() -> some View {
        self
            .background(AppTheme.surface)
            .cornerRadius(AppTheme.Radius.md)
            .shadow(color: Color.black.opacity(0.05), radius: 4, x: 0, y: 2)
    }

    func primaryButtonStyle() -> some View {
        self
            .font(.headline)
            .foregroundColor(.white)
            .frame(maxWidth: .infinity)
            .frame(height: 56)
            .background(AppTheme.primary)
            .cornerRadius(AppTheme.Radius.md)
    }

    func secondaryButtonStyle() -> some View {
        self
            .font(.headline)
            .foregroundColor(AppTheme.primary)
            .frame(maxWidth: .infinity)
            .frame(height: 56)
            .background(AppTheme.surface)
            .overlay(
                RoundedRectangle(cornerRadius: AppTheme.Radius.md)
                    .stroke(AppTheme.primary, lineWidth: 1.5)
            )
            .cornerRadius(AppTheme.Radius.md)
    }
}