import SwiftUI

struct LoginView: View {

    @StateObject private var viewModel = AuthViewModel()

    @State private var email = ""
    @State private var password = ""
    @State private var showPassword = false

    let onLoginSuccess: () -> Void
    let onNavigateToRegister: () -> Void

    var body: some View {
        NavigationView {
            ScrollView {
                VStack(spacing: AppTheme.Spacing.lg) {

                    Spacer().frame(height: 40)

                    // Logo / Title
                    VStack(spacing: AppTheme.Spacing.sm) {
                        Text("🔐")
                            .font(.system(size: 64))

                        Text("SmartControl Pro")
                            .font(.largeTitle)
                            .fontWeight(.bold)
                            .foregroundColor(AppTheme.primary)

                        Text("Consent-Based Device Management")
                            .font(.subheadline)
                            .foregroundColor(AppTheme.onSurfaceVariant)
                    }

                    Spacer().frame(height: 24)

                    // Email
                    VStack(alignment: .leading, spacing: AppTheme.Spacing.xs) {
                        Text("Email")
                            .font(.caption)
                            .foregroundColor(AppTheme.onSurfaceVariant)

                        HStack {
                            Image(systemName: "envelope")
                                .foregroundColor(AppTheme.onSurfaceVariant)

                            TextField("Enter your email", text: $email)
                                .keyboardType(.emailAddress)
                                .autocapitalization(.none)
                                .disableAutocorrection(true)
                        }
                        .padding()
                        .background(AppTheme.surfaceVariant)
                        .cornerRadius(AppTheme.Radius.md)
                    }

                    // Password
                    VStack(alignment: .leading, spacing: AppTheme.Spacing.xs) {
                        Text("Password")
                            .font(.caption)
                            .foregroundColor(AppTheme.onSurfaceVariant)

                        HStack {
                            Image(systemName: "lock")
                                .foregroundColor(AppTheme.onSurfaceVariant)

                            if showPassword {
                                TextField("Enter your password", text: $password)
                            } else {
                                SecureField("Enter your password", text: $password)
                            }

                            Button(action: { showPassword.toggle() }) {
                                Image(systemName: showPassword ? "eye.slash" : "eye")
                                    .foregroundColor(AppTheme.onSurfaceVariant)
                            }
                        }
                        .padding()
                        .background(AppTheme.surfaceVariant)
                        .cornerRadius(AppTheme.Radius.md)
                    }

                    // Error
                    if let error = viewModel.error {
                        Text(error)
                            .font(.caption)
                            .foregroundColor(AppTheme.error)
                            .frame(maxWidth: .infinity, alignment: .leading)
                    }

                    // Login button
                    Button {
                        viewModel.login(email: email, password: password)
                    } label: {
                        if viewModel.isLoading {
                            ProgressView()
                                .progressViewStyle(CircularProgressViewStyle(tint: .white))
                        } else {
                            Text("Login")
                        }
                    }
                    .primaryButtonStyle()
                    .disabled(viewModel.isLoading)

                    // Register link
                    Button {
                        onNavigateToRegister()
                    } label: {
                        Text("Don't have an account? ")
                            .foregroundColor(AppTheme.onSurfaceVariant)
                        + Text("Register")
                            .foregroundColor(AppTheme.primary)
                            .fontWeight(.semibold)
                    }

                    Spacer()
                }
                .padding(AppTheme.Spacing.lg)
            }
            .background(AppTheme.background.ignoresSafeArea())
            .navigationBarHidden(true)
            .onChange(of: viewModel.isLoggedIn) { newValue in
                if newValue { onLoginSuccess() }
            }
        }
    }
}

struct LoginView_Previews: PreviewProvider {
    static var previews: some View {
        LoginView(onLoginSuccess: {}, onNavigateToRegister: {})
    }
}