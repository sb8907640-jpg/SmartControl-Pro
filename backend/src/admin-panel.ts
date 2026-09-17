import express, { type Request, type Response, type NextFunction } from 'express';

export const OWNER_ROLES = new Set(['SUPER_ADMIN', 'OWNER']);

type AuthenticatedRequest = Request & {
  user?: { id: string; role: string };
};

export function requireOwnerAdminPanel(req: AuthenticatedRequest, res: Response, next: NextFunction) {
  const user = req.user;
  if (!user) return res.status(401).json({ error: 'Unauthorized' });
  if (OWNER_ROLES.has(user.role)) return next();

  // Persist this event using the audit_logs repository in production.
  console.warn('[audit]', JSON.stringify({
    actorId: user.id,
    role: user.role,
    action: 'ADMIN_PANEL_ACCESS_DENIED',
    path: req.originalUrl,
    ipAddress: req.ip,
    userAgent: req.get('user-agent'),
    createdAt: new Date().toISOString(),
  }));
  return res.status(403).json({ error: 'Access Denied' });
}

const app = express();
app.use(express.json());

// JWT/session middleware must run before this route in the real backend.
app.get('/api/admin-panel', requireOwnerAdminPanel, (_req, res) => {
  res.json({
    sections: ['team-roles', 'permission-matrix', 'audit-log', 'billing-emi', 'system-settings'],
  });
});

export default app;
