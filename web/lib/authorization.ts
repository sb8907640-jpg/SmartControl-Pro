export const OWNER_ROLES = ['SUPER_ADMIN', 'OWNER'] as const;

export type OwnerRole = (typeof OWNER_ROLES)[number];
export type AppRole = OwnerRole | 'ADMIN' | 'MODERATOR' | 'SUPPORT' | 'FINANCE_ADMIN' | 'LEGAL_ADMIN' | 'USER';

export function canAccessAdminPanel(role: string | null | undefined): role is OwnerRole {
  return role === 'SUPER_ADMIN' || role === 'OWNER';
}
