import { requireOwnerAdminPanelAccess } from '@/lib/admin-panel-access';

export default async function AdminPanelPage() {
  // Replace these values with the verified server session. Do not derive them
  // from browser state, URL parameters, or a client-supplied role.
  const actorId = 'server-session-user-id';
  const role = 'server-session-role';

  await requireOwnerAdminPanelAccess({ actorId, role, path: '/admin-panel' });

  return (
    <main>
      <h1>Admin Panel</h1>
      <p>Owner Settings</p>
      <ul>
        <li>Team / Role management</li>
        <li>Permission matrix</li>
        <li>Full audit log</li>
        <li>Billing / EMI overview</li>
        <li>System settings</li>
      </ul>
    </main>
  );
}
