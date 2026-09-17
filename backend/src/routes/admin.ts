import express from 'express';
import { requireFirebaseAuth } from '../middleware/firebaseAuth';
import { requireOwnerAdminPanel } from '../middleware/roles';
import type { AuditWriter } from '../middleware/roles';
import { assignRole } from '../services/roles';

export default function adminRouter(writeAudit: AuditWriter) {
  const router = express.Router();
  const owner = requireOwnerAdminPanel(writeAudit);
  router.get('/admin-panel', requireFirebaseAuth, owner, (_req, res) => res.json({ sections: ['team-roles', 'permission-matrix', 'audit-log', 'billing-emi', 'system-settings'] }));
  router.post('/admin/users/:uid/role', requireFirebaseAuth, owner, async (req, res) => {
    try { await assignRole(req.user!.id, req.params.uid, req.body.role); res.status(204).end(); }
    catch (error) { res.status(403).json({ error: error instanceof Error ? error.message : 'Role assignment failed' }); }
  });
  return router;
}
