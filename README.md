# SmartControl Pro

Consent-first device management monorepo. This initial scaffold implements the v3.2.0 owner-only Admin Panel visibility rules.

## v3.2.0 Admin Panel rules

- `SUPER_ADMIN` and `OWNER` can see and access Admin Panel.
- All other roles are hidden from the Profile menu and receive `403 Access Denied` on direct route/API access.
- Denied route attempts are recorded as audit events.
- Authorization is enforced server-side; hiding a menu is not treated as security.

See `docs/ADMIN_PANEL.md` for integration guidance.
