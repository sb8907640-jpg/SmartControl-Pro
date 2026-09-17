import express from 'express';
import { requireFirebaseAuth } from '../middleware/firebaseAuth';
import { requireOwnerAdminPanel } from '../middleware/roles';
import type { AuditWriter } from '../middleware/roles';

export default function adminRouter(writeAudit: AuditWriter) {
  const router = express.Router();

  router.get('/admin-panel', requireFirebaseAuth, requireOwnerAdminPanel(writeAudit), (_req, res) => {
    res.json({ sections: ['team-roles', 'permission-matrix', 'audit-log', 'billing-emi', 'system-settings'] });
  });

  return router;
}
