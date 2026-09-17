import { firebaseAuth, firestore } from '../config/firebase';
import type { AppRole } from '../middleware/firebaseAuth';

export async function assignRole(actorUid: string, targetUid: string, role: AppRole) {
  const bootstrap = (process.env.ROLE_BOOTSTRAP_UIDS ?? '').split(',').map(v => v.trim()).filter(Boolean);
  const actor = await firebaseAuth().getUser(actorUid);
  const actorRole = actor.customClaims?.role;
  if (actorRole !== 'SUPER_ADMIN' && actorRole !== 'OWNER' && !bootstrap.includes(actorUid)) {
    throw new Error('Only an owner or configured bootstrap UID may assign roles');
  }
  if (role === 'SUPER_ADMIN' && actorRole !== 'SUPER_ADMIN') throw new Error('Only SUPER_ADMIN may grant SUPER_ADMIN');
  await firebaseAuth().setCustomUserClaims(targetUid, { ...(await firebaseAuth().getUser(targetUid)).customClaims, role });
  await firestore().collection('auditLogs').add({ actorId: actorUid, targetId: targetUid, action: 'ROLE_ASSIGNED', role, createdAt: new Date() });
  await firebaseAuth().revokeRefreshTokens(targetUid);
}
