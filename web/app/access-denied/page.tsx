import Link from 'next/link';

export default function AccessDeniedPage() {
  return (
    <main className="mx-auto max-w-xl p-6 pt-20">
      <h1 className="text-2xl font-bold">Access Denied</h1>
      <p className="mt-3">Admin Panel sirf Owner ya Super Admin ke liye available hai.</p>
      <p className="mt-2">This access attempt has been recorded for audit.</p>
      <Link className="mt-6 inline-block underline" href="/dashboard">Back to Dashboard</Link>
    </main>
  );
}
