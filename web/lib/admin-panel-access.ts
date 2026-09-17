import { canAccessAdminPanel } from './authorization';

export type AuditEvent = {
  actorId: string;
  role: string;
  action: 'ADMIN_PANEL_ACCESS_DENIED';
  path: string;
  ipAddress?: string;
  userAgent?: string;
  createdAt: string;
};

export async function recordAdminPanelAccessDenied(event: Omit<AuditEvent, 'action' | 'createdAt'>): Promise<AuditEvent> {
  const auditEvent: AuditEvent = {
    ...event,
    action: 'ADMIN_PANEL_ACCESS_DENIED',
    createdAt: new Date().toISOString(),
  };

  // Replace this adapter with the PostgreSQL audit_logs repository in production.
  console.warn('[audit]', JSON.stringify(auditEvent));
  return auditEvent;
}

export async function requireOwnerAdminPanelAccess(input: {
  actorId: string;
  role: string;
  path: string;
  ipAddress?: string;
  userAgent?: string;
}): Promise<void> {
  if (canAccessAdminPanel(input.role)) return;

  await recordAdminPanelAccessDenied(input);
  const error = new Error('Access Denied');
  Object.assign(error, { statusCode: 403 });
  throw error;
}
