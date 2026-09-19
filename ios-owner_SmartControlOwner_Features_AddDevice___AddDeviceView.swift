import SwiftUI

struct AddDeviceView: View {

    @StateObject private var viewModel = AddDeviceViewModel()
    @Environment(\.dismiss) private var dismiss

    let onGenerateInvite: (InviteLink) -> Void

    var body: some View {
        NavigationView {
            ZStack {
                AppTheme.background.ignoresSafeArea()

                ScrollView {
                    VStack(alignment: .leading, spacing: AppTheme.Spacing.lg) {

                        // Device type
                        sectionHeader("Device Type", "Auto-detect recommended hai")

                        VStack(spacing: 0) {
                            DeviceTypeRow(
                                icon: "🔄",
                                label: "Auto-detect (Recommended)",
                                selected: viewModel.selectedDeviceType == nil,
                                action: { viewModel.selectDeviceType(nil) }
                            )

                            ForEach(Constants.deviceTypes) { type in
                                DeviceTypeRow(
                                    icon: type.icon,
                                    label: type.label,
                                    selected: viewModel.selectedDeviceType == type.id,
                                    action: { viewModel.selectDeviceType(type.id) }
                                )
                            }
                        }
                        .background(AppTheme.surface)
                        .cornerRadius(AppTheme.Radius.md)

                        // Permissions
                        HStack {
                            sectionHeader(
                                "Permissions (\(viewModel.selectedPermissions.count)/\(Constants.allPermissions.count))",
                                "Receiver ye permissions allow/deny kar sakta hai"
                            )
                        }

                        HStack(spacing: AppTheme.Spacing.sm) {
                            Button("Select All") { viewModel.selectAllPermissions() }
                                .font(.subheadline)
                                .frame(maxWidth: .infinity)
                                .padding(.vertical, 10)
                                .background(AppTheme.surfaceVariant)
                                .cornerRadius(AppTheme.Radius.sm)

                            Button("Clear All") { viewModel.deselectAllPermissions() }
                                .font(.subheadline)
                                .frame(maxWidth: .infinity)
                                .padding(.vertical, 10)
                                .background(AppTheme.surfaceVariant)
                                .cornerRadius(AppTheme.Radius.sm)
                        }
                        .foregroundColor(AppTheme.primary)

                        VStack(spacing: AppTheme.Spacing.sm) {
                            ForEach(Constants.allPermissions) { permission in
                                PermissionRow(
                                    permission: permission,
                                    isSelected: viewModel.selectedPermissions.contains(permission.id),
                                    onToggle: { viewModel.togglePermission(permission.id) }
                                )
                            }
                        }

                        // Expiry
                        sectionHeader("Link Expiry", "Link expire hone ke baad valid nahi hoga")

                        VStack(spacing: 0) {
                            ForEach(Constants.expiryOptions) { option in
                                ExpiryRow(
                                    label: option.label,
                                    selected: viewModel.selectedExpiryHours == option.hours,
                                    action: { viewModel.selectExpiry(hours: option.hours) }
                                )
                            }
                        }
                        .background(AppTheme.surface)
                        .cornerRadius(AppTheme.Radius.md)

                        // Error
                        if let error = viewModel.error {
                            Text(error)
                                .font(.caption)
                                .foregroundColor(AppTheme.error)
                                .padding()
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .background(AppTheme.error.opacity(0.1))
                                .cornerRadius(AppTheme.Radius.sm)
                        }

                        Spacer(minLength: 20)
                    }
                    .padding(AppTheme.Spacing.lg)
                }

                // Bottom button
                VStack {
                    Spacer()

                    VStack(spacing: AppTheme.Spacing.sm) {
                        Button {
                            viewModel.generateInvite { invite in
                                onGenerateInvite(invite)
                            }
                        } label: {
                            if viewModel.isLoading {
                                ProgressView()
                                    .progressViewStyle(CircularProgressViewStyle(tint: .white))
                            } else {
                                Text("Generate Link")
                            }
                        }
                        .primaryButtonStyle()
                        .disabled(viewModel.isLoading || viewModel.selectedPermissions.isEmpty)

                        Button("Cancel") { dismiss() }
                            .frame(maxWidth: .infinity)
                            .frame(height: 48)
                    }
                    .padding()
                    .background(AppTheme.surface)
                    .shadow(color: .black.opacity(0.05), radius: 4, y: -2)
                }
            }
            .navigationTitle("Add New Device")
            .navigationBarTitleDisplayMode(.inline)
        }
    }

    private func sectionHeader(_ title: String, _ subtitle: String) -> some View {
        VStack(alignment: .leading, spacing: 2) {
            Text(title)
                .font(.headline)
                .fontWeight(.semibold)

            Text(subtitle)
                .font(.caption)
                .foregroundColor(AppTheme.onSurfaceVariant)
        }
    }
}

struct DeviceTypeRow: View {
    let icon: String
    let label: String
    let selected: Bool
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            HStack {
                Text(icon)
                    .font(.title3)

                Text(label)
                    .foregroundColor(AppTheme.onSurface)

                Spacer()

                Image(systemName: selected ? "largecircle.fill.circle" : "circle")
                    .foregroundColor(selected ? AppTheme.primary : AppTheme.onSurfaceVariant)
            }
            .padding()
        }
        .buttonStyle(.plain)
    }
}

struct ExpiryRow: View {
    let label: String
    let selected: Bool
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            HStack {
                Text(label)
                    .foregroundColor(AppTheme.onSurface)

                Spacer()

                Image(systemName: selected ? "largecircle.fill.circle" : "circle")
                    .foregroundColor(selected ? AppTheme.primary : AppTheme.onSurfaceVariant)
            }
            .padding()
        }
        .buttonStyle(.plain)
    }
}