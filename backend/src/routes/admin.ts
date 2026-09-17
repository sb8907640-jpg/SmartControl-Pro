import express from 'express';
import { requireOwnerAdminPanel } from '../middleware/requireOwnerAdminPanel';

const router = express.Router();

const writeAudit = async (event: Parameters<ReturnType<typeof requireOwnerAdminPanel>>[0] extends never ? never : {
  actorId: string;
  role: string;
  action: 'ADMIN_PANEL_ACCESS_DENIED';
  path: string;
  ipAddress?: string;
  userAgent?: string;
}) => {
  // Replace with a parameterized PostgreSQL repository call.
  console.warn('[audit] ADMIN_PANEL_ACCESS_DENIED', JSON.stringify({ ...event, createdAt: new Date().toISOString() }));
};

router.get('/admin-panel', requireOwnerAdminPanel(writeAudit), (_req, res) => {
  res.json({ sections: ['team-roles', 'permission-matrix', 'audit-log', 'billing-emi', 'system-settings'] });
});

export default router;
