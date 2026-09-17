# Production auth and release setup

## Backend

- Copy `.env.example` to `backend/.env` and fill Firebase Admin credentials.
- Clients send a Firebase ID token as `Authorization: Bearer <token>`.
- `requireFirebaseAuth` verifies the token, checks revocation, and reads the trusted `role` custom claim.
- Roles are assigned only through the owner-protected route or a controlled bootstrap UID. Do not expose service-account credentials to clients.
- After role assignment, the target user must sign in again or refresh its ID token.

## Android

- Create a Firebase Android app with package `com.smartcontrol.pro`.
- Put the downloaded `google-services.json` in `android/app/`.
- Enable Google and Phone providers in Firebase Authentication.
- The `AuthViewModel` handles Firebase sessions and refreshes custom claims. Wire the activity-result Google credential flow and Firebase `PhoneAuthProvider` callbacks into `AuthScreen` before release; the checked-in UI intentionally contains no fake credential or phone OTP.

## Release APK

Set `ANDROID_KEYSTORE_FILE`, `ANDROID_KEYSTORE_PASSWORD`, `ANDROID_KEY_ALIAS`, and `ANDROID_KEY_PASSWORD` as local environment variables or CI secrets. Generate a keystore with `keytool`, then run:

`./gradlew :app:assembleRelease`

Never commit the keystore, passwords, Firebase Admin private key, or `google-services.json` unless your organization explicitly permits it.
