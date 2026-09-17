import type { NextRequest } from 'next/server';
import { NextResponse } from 'next/server';

/**
 * Defense-in-depth route guard. The API must repeat this authorization check;
 * middleware must never be the only security boundary.
 */
export function middleware(request: NextRequest) {
  if (!request.nextUrl.pathname.startsWith('/admin-panel')) {
    return NextResponse.next();
  }

  // The authenticated role should be set by the trusted auth/session layer.
  // Never trust a client-controlled query parameter or localStorage value.
  const role = request.headers.get('x-authenticated-role');
  if (role !== 'SUPER_ADMIN' && role !== 'OWNER') {
    return NextResponse.redirect(new URL('/access-denied?resource=admin-panel', request.url));
  }

  return NextResponse.next();
}

export const config = { matcher: ['/admin-panel/:path*'] };
