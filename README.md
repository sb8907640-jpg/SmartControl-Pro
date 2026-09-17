# SmartControl Pro

Consent-first device-management platform scaffold.

## v3.2.0 owner-only Admin Panel

The Admin Panel is available only to verified `SUPER_ADMIN` and `OWNER` sessions.

- Non-owner users do not receive an Admin Panel menu item.
- Direct page and API access returns `403 Access Denied`.
- Every denied attempt is written to the audit-log adapter.
- Authorization is enforced server-side; UI hiding is not a security boundary.

This commit contains the combined web, backend, Android, database, and test patch. Replace the example session and audit adapters with the project's production Firebase/JWT and PostgreSQL implementations before deployment.
