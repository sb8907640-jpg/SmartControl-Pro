import { redirect } from 'next/navigation';
import { requireOwnerAdminPanelAccess } from '@/lib/admin-panel-access';

// Replace this adapter with the verified Firebase/JWT session implementation.
async function getVerifiedSession(): Promise<{ userId: string; role: string } | null> {
  return null;
}

export default async function AdminPanelPage() {
  const session = await getVerifiedSession();
  if (!session) redirect('/login');

  try {
    await requireOwnerAdminPanelAccess({
      actorId: session.userId,
      role: session.role,
      path: '/admin-panel',
    });
  } catch {
    redirect('/access-denied?resource=admin-panel');
  }

  return (
    <main className="p-6">
      <h1 className="text-2xl font-bold">Admin Panel</h1>
      <p>Owner Settings</p>
      <ul className="mt-4 list-disc pl-6">
        <li>Team / Role management</li>
        <li>Owner system edit and transfer</li>
        <li>Permission matrix</li>
        <li>Full audit log</li>
        <li>Billing / EMI overview</li>
        <li>System settings</li>
      </ul>
    </main>
  );
}
