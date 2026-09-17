import { getAuth } from 'firebase-admin/auth';
import type { AppRole } from './middleware/firebaseAuth';

export async function setUserRole(uid: string, role: AppRole) {
  await getAuth().setCustomUserClaims(uid, { role });
}

export async function revokeUserSessions(uid: string) {
  await getAuth().revokeRefreshTokens(uid);
}
