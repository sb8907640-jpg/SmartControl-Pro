import Link from 'next/link';

export default function AccessDeniedPage() {
  return (
    <main style={{ maxWidth: 560, margin: '80px auto', padding: 24, fontFamily: 'sans-serif' }}>
      <h1>Access Denied</h1>
      <p>Admin Panel sirf Owner ya Super Admin ke liye available hai.</p>
      <Link href="/dashboard">Back to Dashboard</Link>
    </main>
  );
}
