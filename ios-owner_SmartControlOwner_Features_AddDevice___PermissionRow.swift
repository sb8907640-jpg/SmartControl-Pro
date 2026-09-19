import SwiftUI

struct PermissionRow: View {

    let permission: Constants.Permission
    let isSelected: Bool
    let onToggle: () -> Void

    var body: some View {
        Button(action: onToggle) {
            HStack(spacing: AppTheme.Spacing.md) {
                Text(permission.icon)
                    .font(.title2)

                VStack(alignment: .leading, spacing: 2) {
                    Text(permission.title)
                        .font(.subheadline)
                        .fontWeight(.semibold)
                        .foregroundColor(AppTheme.onSurface)

                    Text(permission.description)
                        .font(.caption)
                        .foregroundColor(AppTheme.onSurfaceVariant)
                }

                Spacer()

                Image(systemName: isSelected ? "checkmark.square.fill" : "square")
                    .foregroundColor(isSelected ? AppTheme.primary : AppTheme.onSurfaceVariant)
                    .font(.title3)
            }
            .padding()
            .background(isSelected ? AppTheme.primary.opacity(0.08) : AppTheme.surface)
            .cornerRadius(AppTheme.Radius.md)
            .overlay(
                RoundedRectangle(cornerRadius: AppTheme.Radius.md)
                    .stroke(isSelected ? AppTheme.primary.opacity(0.3) : Color.clear, lineWidth: 1.5)
            )
        }
        .buttonStyle(.plain)
    }
}