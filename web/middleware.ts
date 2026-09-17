import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';

export function middleware(request: NextRequest) {
  if (!request.nextUrl.pathname.startsWith('/admin-panel')) {
    return NextResponse.next();
  }

  // This is only defense in depth. The authoritative role check must use the
  // verified server session in the page and API route; never trust a client
  // header, query parameter, localStorage value, or request body for identity.
  return NextResponse.next();
}

export const config = { matcher: ['/admin-panel/:path*'] };
