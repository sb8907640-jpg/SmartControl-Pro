import type { Request, Response, NextFunction } from 'express';
import type { AuthenticatedRequest, AppRole } from './firebaseAuth';

export type AuditWriter = (event: {
  actorId: string;
  role: AppRole;
  action: string;
  path: string;
  ipAddress?: string;
  userAgent?: string;
}) => Promise<void>;

export function requireRoles(allowed: readonly AppRole[], auditWriter: AuditWriter) {
  return async (req: AuthenticatedRequest, res: Response, next: NextFunction) => {
    const user = req.user;
    if (!user) return res.status(401).json({ error: 'Unauthorized' });
    if (allowed.includes(user.role)) return next();

    await auditWriter({
      actorId: user.id,
      role: user.role,
      action: 'ACCESS_DENIED',
      path: req.originalUrl,
      ipAddress: req.ip,
      userAgent: req.get('user-agent'),
    });
    return res.status(403).json({ error: 'Access Denied' });
  };
}

export function requireOwnerAdminPanel(auditWriter: AuditWriter) {
  return requireRoles(['SUPER_ADMIN', 'OWNER'], auditWriter);
}
