# End-to-end wiring notes

## Backend

1. Set `FIREBASE_PROJECT_ID`, `FIREBASE_CLIENT_EMAIL`, and `FIREBASE_PRIVATE_KEY` as server secrets.
2. Run Firebase Admin initialization before registering routes.
3. Attach `requireFirebaseAuth` before protected routes. Clients send `Authorization: Bearer <Firebase ID token>`.
4. Assign roles only from a trusted owner/admin service using Firebase custom claims; clients must never set their own role.
5. Mount `admin` after authentication and replace the example audit writer with a parameterized PostgreSQL insert.

## Firestore

Deploy `firebase/firestore.rules` with Firebase CLI. Rules are deny-by-default and require authenticated owner/receiver relationships.

## Android

`SmartControlApp` provides the connected five-tab Compose shell. Connect the button callbacks to authenticated repositories using the Firebase ID token and backend API. The controls shown here are consent/status UI; sensitive device capabilities must remain disabled until the receiver grants the relevant Android runtime/system permission and a visible session is active.

## Honest build status

This repository is a scaffold and does not yet contain Gradle, package-lock, Firebase config, production session adapter, or database repository implementations. Those environment-specific pieces are required before claiming a signed APK or production end-to-end operation.
