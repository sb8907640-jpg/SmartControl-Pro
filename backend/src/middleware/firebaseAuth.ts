import type { NextFunction, Request, Response } from 'express';
import { getApps, initializeApp, cert, type ServiceAccount } from 'firebase-admin/app';
import { getAuth } from 'firebase-admin/auth';

if (!getApps().length) {
  const serviceAccount: ServiceAccount = {
    projectId: process.env.FIREBASE_PROJECT_ID,
    clientEmail: process.env.FIREBASE_CLIENT_EMAIL,
    privateKey: process.env.FIREBASE_PRIVATE_KEY?.replace(/\\n/g, '\n'),
  };

  if (!serviceAccount.projectId || !serviceAccount.clientEmail || !serviceAccount.privateKey) {
    throw new Error('Firebase Admin credentials are required');
  }
  initializeApp({ credential: cert(serviceAccount) });
}

export type AppRole = 'SUPER_ADMIN' | 'OWNER' | 'ADMIN' | 'MODERATOR' | 'SUPPORT' | 'FINANCE_ADMIN' | 'LEGAL_ADMIN' | 'USER';
export type AuthenticatedUser = { id: string; role: AppRole; email?: string };
export type AuthenticatedRequest = Request & { user?: AuthenticatedUser };

function isRole(value: unknown): value is AppRole {
  return typeof value === 'string' && ['SUPER_ADMIN', 'OWNER', 'ADMIN', 'MODERATOR', 'SUPPORT', 'FINANCE_ADMIN', 'LEGAL_ADMIN', 'USER'].includes(value);
}

export function requireFirebaseAuth(req: AuthenticatedRequest, res: Response, next: NextFunction) {
  void authenticateRequest(req, res, next);
}

async function authenticateRequest(req: AuthenticatedRequest, res: Response, next: NextFunction) {
  const header = req.header('authorization');
  if (!header?.startsWith('Bearer ')) return res.status(401).json({ error: 'Unauthorized' });

  try {
    const token = await getAuth().verifyIdToken(header.slice('Bearer '.length), true);
    const roleClaim = token.role ?? token.adminRole ?? 'USER';
    if (!isRole(roleClaim)) return res.status(403).json({ error: 'Invalid role' });
    req.user = { id: token.uid, role: roleClaim, email: token.email };
    return next();
  } catch {
    return res.status(401).json({ error: 'Invalid or expired token' });
  }
}
