# Admin Panel v3.2.0

## Rules

Only verified `SUPER_ADMIN` and `OWNER` roles may see or access Admin Panel. The Profile menu item is omitted completely for every other role.

Every direct page/API request is checked again on the server. A non-owner receives HTTP `403 Access Denied`, and an `ADMIN_PANEL_ACCESS_DENIED` event is recorded with actor, role, path, timestamp, IP where available, and user agent.

## Owner-only sections

- Team / role management
- Owner edit, delete, and ownership transfer
- Permission matrix
- Full audit log
- Billing / EMI overview
- System settings

## Production integration checklist

1. Implement `getVerifiedSession()` using Firebase Auth or a verified JWT/session cookie.
2. Attach the verified user to `req.user` in the backend auth middleware.
3. Replace the example audit writer with a parameterized PostgreSQL insert.
4. Do not trust role data from localStorage, URL parameters, client headers, or request bodies.
5. Run the authorization unit tests and API integration tests before deployment.
