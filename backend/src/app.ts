import 'dotenv/config';
import express from 'express';
import cors from 'cors';
import adminRouter from './routes/admin';
import { firebaseAuth, firestore } from './config/firebase';
import type { AuditWriter } from './middleware/roles';

export const app = express();
app.set('trust proxy', 1);
app.use(cors({ origin: process.env.CORS_ORIGIN?.split(',') ?? true, credentials: true }));
app.use(express.json({ limit: '1mb' }));

app.get('/health', (_req, res) => res.json({ ok: true, service: 'smartcontrol-backend' }));

export const writeAudit: AuditWriter = async (event) => {
  await firestore().collection('auditLogs').add({
    ...event,
    createdAt: new Date(),
  });
};

// Admin routes verify the Firebase ID token and then enforce the role claim.
app.use('/api', adminRouter(writeAudit));

export function startServer(port = Number(process.env.PORT ?? 8080)) {
  // Fail early during startup if credentials are not configured.
  firebaseAuth();
  return app.listen(port, () => console.log(`SmartControl API listening on ${port}`));
}

if (require.main === module) startServer();
