import { NextResponse } from 'next/server';
import { requireOwnerAdminPanelAccess } from '@/lib/admin-panel-access';

async function getVerifiedSession(request: Request): Promise<{ userId: string; role: string } | null> {
  // Verify the signed session/JWT here. Do not read role from client headers.
  void request;
  return null;
}

export async function GET(request: Request) {
  const session = await getVerifiedSession(request);
  if (!session) return NextResponse.json({ error: 'Unauthorized' }, { status: 401 });

  try {
    await requireOwnerAdminPanelAccess({
      actorId: session.userId,
      role: session.role,
      path: new URL(request.url).pathname,
      ipAddress: request.headers.get('x-forwarded-for') ?? undefined,
      userAgent: request.headers.get('user-agent') ?? undefined,
    });
  } catch (error) {
    if (error instanceof Error && 'statusCode' in error && error.statusCode === 403) {
      return NextResponse.json({ error: 'Access Denied' }, { status: 403 });
    }
    return NextResponse.json({ error: 'Internal Server Error' }, { status: 500 });
  }

  return NextResponse.json({
    sections: ['team-roles', 'permission-matrix', 'audit-log', 'billing-emi', 'system-settings'],
  });
}
