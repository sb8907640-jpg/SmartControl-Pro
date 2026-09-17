import 'dotenv/config';
import cors from 'cors';
import express from 'express';
import adminRouter from './routes/admin';
import { firebaseAuth, firestore } from './config/firebase';
import type { AuditWriter } from './middleware/roles';

export const app = express();
app.set('trust proxy', 1);
app.use(cors({ origin: process.env.CORS_ORIGIN?.split(',') ?? true, credentials: true }));
app.use(express.json({ limit: '1mb' }));
app.get('/health', (_req, res) => res.json({ ok: true }));
export const writeAudit: AuditWriter = async event => { await firestore().collection('auditLogs').add({ ...event, createdAt: new Date() }); };
app.use('/api', adminRouter(writeAudit));
export function startServer(port = Number(process.env.PORT ?? 8080)) { firebaseAuth(); return app.listen(port, () => console.log(`API listening on ${port}`)); }
if (require.main === module) startServer();
