import { canAccessAdminPanel } from './authorization';

describe('Admin Panel authorization', () => {
  it.each(['SUPER_ADMIN', 'OWNER'])('allows %s', role => {
    expect(canAccessAdminPanel(role)).toBe(true);
  });

  it.each(['ADMIN', 'MODERATOR', 'SUPPORT', 'FINANCE_ADMIN', 'LEGAL_ADMIN', 'USER', '', 'owner', null, undefined])('denies %s', role => {
    expect(canAccessAdminPanel(role)).toBe(false);
  });
});
