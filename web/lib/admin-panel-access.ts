import { canAccessAdminPanel } from './authorization';

export type DeniedAccessInput = {
  actorId: string;
  role: string;
  path: string;
  ipAddress?: string;
  userAgent?: string;
};

export async function recordAdminPanelAccessDenied(input: DeniedAccessInput) {
  const event = {
    ...input,
    action: 'ADMIN_PANEL_ACCESS_DENIED' as const,
    createdAt: new Date().toISOString(),
  };

  // Production adapter: insert event into PostgreSQL audit_logs.
  console.warn('[audit]', JSON.stringify(event));
  return event;
}

export async function requireOwnerAdminPanelAccess(input: DeniedAccessInput) {
  if (canAccessAdminPanel(input.role)) return;

  await recordAdminPanelAccessDenied(input);
  const error = new Error('Access Denied') as Error & { statusCode: number };
  error.statusCode = 403;
  throw error;
}
