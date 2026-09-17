import type { NextFunction, Request, Response } from 'express';
import { firebaseAuth } from '../config/firebase';

export const ROLES = ['SUPER_ADMIN', 'OWNER', 'ADMIN', 'MODERATOR', 'SUPPORT', 'FINANCE_ADMIN', 'LEGAL_ADMIN', 'USER'] as const;
export type AppRole = (typeof ROLES)[number];
export type AuthenticatedRequest = Request & { user?: { id: string; role: AppRole; email?: string } };

const isRole = (value: unknown): value is AppRole => typeof value === 'string' && (ROLES as readonly string[]).includes(value);

export async function requireFirebaseAuth(req: AuthenticatedRequest, res: Response, next: NextFunction) {
  const header = req.get('authorization');
  if (!header?.startsWith('Bearer ')) return res.status(401).json({ error: 'Unauthorized' });
  try {
    const decoded = await firebaseAuth().verifyIdToken(header.slice(7), true);
    const role = decoded.role ?? 'USER';
    if (!isRole(role)) return res.status(403).json({ error: 'Invalid role claim' });
    req.user = { id: decoded.uid, role, email: decoded.email };
    return next();
  } catch {
    return res.status(401).json({ error: 'Invalid or expired Firebase token' });
  }
}
