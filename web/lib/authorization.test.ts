import { canAccessAdminPanel } from '../web/lib/authorization';

describe('Admin Panel authorization', () => {
  it.each(['SUPER_ADMIN', 'OWNER'])('allows %s', (role) => {
    expect(canAccessAdminPanel(role)).toBe(true);
  });

  it.each(['ADMIN', 'MODERATOR', 'SUPPORT', 'FINANCE_ADMIN', 'LEGAL_ADMIN', 'USER', '', 'owner'])('hides %s', (role) => {
    expect(canAccessAdminPanel(role)).toBe(false);
  });
});
