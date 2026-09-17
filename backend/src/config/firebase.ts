import { cert, getApps, initializeApp, type App } from 'firebase-admin/app';
import { getAuth, type Auth } from 'firebase-admin/auth';
import { getFirestore, type Firestore } from 'firebase-admin/firestore';

let app: App | undefined;

export function getFirebaseApp(): App {
  if (app) return app;
  if (getApps().length) return (app = getApps()[0]!);
  const projectId = process.env.FIREBASE_PROJECT_ID;
  const clientEmail = process.env.FIREBASE_CLIENT_EMAIL;
  const privateKey = process.env.FIREBASE_PRIVATE_KEY?.replace(/\\n/g, '\n');
  if (!projectId || !clientEmail || !privateKey) throw new Error('Firebase Admin credentials are not configured');
  return (app = initializeApp({ credential: cert({ projectId, clientEmail, privateKey }) }));
}

export const firebaseAuth = (): Auth => getAuth(getFirebaseApp());
export const firestore = (): Firestore => getFirestore(getFirebaseApp());
