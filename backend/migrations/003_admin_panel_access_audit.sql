CREATE INDEX IF NOT EXISTS audit_logs_admin_panel_denied_idx
ON audit_logs (action, created_at);

-- The application inserts complete actor/path/IP/user-agent metadata using a
-- parameterized query whenever a non-owner requests Admin Panel access.
