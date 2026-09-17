# Android setup

1. Install JDK 17 and Android SDK API 35.
2. Create a Firebase Android app with package `com.smartcontrol.pro`.
3. Download `google-services.json` and place it at `android/app/google-services.json` (never commit it if your policy forbids it).
4. Run `firebase deploy --only firestore:rules` from the repository root after selecting your Firebase project.
5. Copy `.env.example` to `backend/.env`, fill the Firebase Admin service-account values, then run the backend with Node 20.
6. The Android app uses the Firebase client SDK; Admin credentials must remain server-side.

The checked-in Android UI is a runnable Compose shell. Production authentication should replace the demo `AppRole.OWNER` in `MainActivity` with the signed-in user's role from a trusted profile/custom claim, and all API calls must send the Firebase ID token as `Authorization: Bearer <token>`.
