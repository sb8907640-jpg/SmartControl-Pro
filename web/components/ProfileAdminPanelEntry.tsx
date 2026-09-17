import Link from 'next/link';
import { canAccessAdminPanel } from '@/lib/authorization';

export function ProfileAdminPanelEntry({ role }: { role: string | null | undefined }) {
  // Deliberately absent, rather than disabled, for every non-owner role.
  if (!canAccessAdminPanel(role)) return null;

  return (
    <Link href="/admin-panel" aria-label="Admin Panel" className="flex items-center gap-2 py-3 font-semibold">
      <span aria-hidden="true">🔒</span>
      Admin Panel
    </Link>
  );
}
