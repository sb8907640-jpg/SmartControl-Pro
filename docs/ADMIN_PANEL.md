# Admin Panel v3.2.0

## Visibility

The Profile tab renders the Admin Panel entry only when the verified session role is `SUPER_ADMIN` or `OWNER`. For `ADMIN`, `MODERATOR`, `SUPPORT`, `FINANCE_ADMIN`, `LEGAL_ADMIN`, and `USER`, the entry is not rendered at all.

## Security

UI hiding is only a usability rule. Every server route must enforce the same allowlist using a verified JWT/session claim. Do not trust role values from the browser, query string, local storage, or ordinary request body fields.

A non-owner direct request returns HTTP `403` with `Access Denied` and creates an `ADMIN_PANEL_ACCESS_DENIED` audit event containing actor, role, path, timestamp, IP (where available), and user agent.

## Owner-only sections

- Team and role management
- Owner system edit, delete, and transfer
- Permission matrix
- Full audit log
- Billing / EMI overview
- System settings

## Integration notes

- Replace the example server-session placeholders with the project's verified Firebase/JWT session adapter.
- Replace `console.warn` with a parameterized insert into `audit_logs`.
- Do not expose service-account credentials in the client or repository.
