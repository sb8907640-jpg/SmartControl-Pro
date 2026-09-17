import express from 'express';
import { requireFirebaseAuth } from '../middleware/firebaseAuth';
import { requireOwnerAdminPanel } from '../middleware/roles';

const router = express.Router();

const writeAudit = async (event: Parameters<Parameters<typeof requireOwnerAdminPanel>[0]>[0] extends never ? never : {
  actorId: string; role: 'SUPER_ADMIN' | 'OWNER' | 'ADMIN' | 'MODERATOR' | 'SUPPORT' | 'FINANCE_ADMIN' | 'LEGAL_ADMIN' | 'USER'; action: string; path: string; ipAddress?: string; userAgent?: string;
}) => {
  // Replace with a parameterized INSERT into audit_logs.
  console.warn('[audit]', JSON.stringify({ ...event, createdAt: new Date().toISOString() }));
};

router.get('/admin-panel', requireFirebaseAuth, requireOwnerAdminPanel(writeAudit), (_req, res) => {
  res.json({ sections: ['team-roles', 'permission-matrix', 'audit-log', 'billing-emi', 'system-settings'] });
});

export default router;
