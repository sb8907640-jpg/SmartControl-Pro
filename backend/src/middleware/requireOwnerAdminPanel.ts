import type { Request, Response, NextFunction } from 'express';

export type AuthenticatedRequest = Request & {
  user?: { id: string; role: string };
};

export type AuditWriter = (event: {
  actorId: string;
  role: string;
  action: 'ADMIN_PANEL_ACCESS_DENIED';
  path: string;
  ipAddress?: string;
  userAgent?: string;
}) => Promise<void>;

export function requireOwnerAdminPanel(auditWriter: AuditWriter) {
  return async (req: AuthenticatedRequest, res: Response, next: NextFunction) => {
    const user = req.user;
    if (!user) return res.status(401).json({ error: 'Unauthorized' });
    if (user.role === 'SUPER_ADMIN' || user.role === 'OWNER') return next();

    await auditWriter({
      actorId: user.id,
      role: user.role,
      action: 'ADMIN_PANEL_ACCESS_DENIED',
      path: req.originalUrl,
      ipAddress: req.ip,
      userAgent: req.get('user-agent'),
    });

    return res.status(403).json({ error: 'Access Denied' });
  };
}
