-- Add this event type to the existing audit_logs model/migration.
-- The exact audit_logs columns may be extended to match the project's common
-- actor/path/IP metadata columns.
INSERT INTO audit_logs (action, metadata, created_at)
VALUES (
  'ADMIN_PANEL_ACCESS_DENIED',
  '{"resource":"admin-panel"}'::jsonb,
  NOW()
);
