import { NextResponse } from 'next/server';
import { requireOwnerAdminPanelAccess } from '@/lib/admin-panel-access';

export async function GET(request: Request) {
  // In production, actor identity and role come from the verified session/JWT.
  const actorId = request.headers.get('x-authenticated-user-id');
  const role = request.headers.get('x-authenticated-role');

  if (!actorId || !role) {
    return NextResponse.json({ error: 'Unauthorized' }, { status: 401 });
  }

  try {
    await requireOwnerAdminPanelAccess({
      actorId,
      role,
      path: new URL(request.url).pathname,
      ipAddress: request.headers.get('x-forwarded-for') ?? undefined,
      userAgent: request.headers.get('user-agent') ?? undefined,
    });

    return NextResponse.json({
      sections: ['team-roles', 'permission-matrix', 'audit-log', 'billing-emi', 'system-settings'],
    });
  } catch (error) {
    if (error instanceof Error && 'statusCode' in error && error.statusCode === 403) {
      return NextResponse.json({ error: 'Access Denied' }, { status: 403 });
    }
    return NextResponse.json({ error: 'Internal Server Error' }, { status: 500 });
  }
}
