import Link from 'next/link';
import { canAccessAdminPanel } from '@/lib/authorization';

export function ProfileAdminPanelEntry({ role }: { role: string }) {
  // Hidden, not disabled, for every non-owner role.
  if (!canAccessAdminPanel(role)) return null;

  return (
    <Link href="/admin-panel" aria-label="Admin Panel">
      <span aria-hidden="true">🔒</span> Admin Panel
    </Link>
  );
}
