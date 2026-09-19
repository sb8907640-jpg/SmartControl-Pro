const express = require('express');
const http = require('http');
const { Server } = require('socket.io');
const cors = require('cors');
const helmet = require('helmet');
const rateLimit = require('express-rate-limit');
require('dotenv').config();

const app = express();
const server = http.createServer(app);

// ============================================
// SECURITY MIDDLEWARE
// ============================================
app.use(helmet());
app.use(cors({
  origin: process.env.ALLOWED_ORIGINS?.split(',') || '*',
  credentials: true
}));
app.use(express.json({ limit: '50mb' }));
app.use(express.urlencoded({ extended: true, limit: '50mb' }));

// Rate limiting
const limiter = rateLimit({
  windowMs: 15 * 60 * 1000, // 15 minutes
  max: 100, // 100 requests per window
  message: 'Too many requests, please try again later.'
});
app.use('/api/', limiter);

// ============================================
// SOCKET.IO
// ============================================
const io = new Server(server, {
  cors: {
    origin: process.env.ALLOWED_ORIGINS?.split(',') || '*',
    credentials: true
  },
  maxHttpBufferSize: 1e8 // 100MB for file transfer
});

// ============================================
// HEALTH CHECK
// ============================================
app.get('/health', (req, res) => {
  res.json({
    status: 'ok',
    version: '6.0.0',
    timestamp: new Date().toISOString()
  });
});

// ============================================
// ROUTES
// ============================================
app.use('/api/auth', require('./routes/auth'));
app.use('/api/devices', require('./routes/devices'));
app.use('/api/consent', require('./routes/consent'));
app.use('/api/link', require('./routes/link'));
app.use('/api/location', require('./routes/location'));
app.use('/api/camera', require('./routes/camera'));
app.use('/api/offline', require('./routes/offline'));
app.use('/api/data', require('./routes/data'));

// ============================================
// SOCKET.IO AUTH + HANDLERS
// ============================================
io.use(require('./middleware/socketAuth'));

io.on('connection', (socket) => {
  const userId = socket.user.id;
  const deviceId = socket.user.deviceId;

  console.log(`[Socket] User ${userId} connected (Device: ${deviceId})`);

  // Join user room
  socket.join(`user:${userId}`);
  if (deviceId) socket.join(`device:${deviceId}`);

  // Consent-based events
  socket.on('consent:request', async (data) => {
    // Verify consent before processing
    const hasConsent = await checkConsent(deviceId, data.feature);
    if (!hasConsent) {
      socket.emit('consent:denied', {
        feature: data.feature,
        message: 'Consent not granted'
      });
      return;
    }

    io.to(`user:${data.ownerId}`).emit('consent:request', {
      ...data,
      from: deviceId
    });
  });

  // Touch control (consent-based)
  socket.on('touch:event', async (data) => {
    const hasConsent = await checkConsent(data.deviceId, 'touch_control');
    if (!hasConsent) return;

    // Audit log
    await logAudit(userId, 'TOUCH_EVENT', data.deviceId, data);

    socket.to(`device:${data.deviceId}`).emit('touch:event', data);
  });

  // STOP button (always works — no consent needed to stop)
  socket.on('stop:all', async (data) => {
    await logAudit(userId, 'STOP_ALL', data.deviceId, {});
    io.to(`device:${data.deviceId}`).emit('stop:all', {
      stoppedBy: userId,
      timestamp: new Date().toISOString()
    });
  });

  socket.on('disconnect', () => {
    console.log(`[Socket] User ${userId} disconnected`);
  });
});

// Helper
async function checkConsent(deviceId, feature) {
  const db = require('./config/database');
  const result = await db.query(
    `SELECT granted FROM consents 
     WHERE device_id = $1 AND feature_name = $2 
     ORDER BY granted_at DESC LIMIT 1`,
    [deviceId, feature]
  );
  return result.rows[0]?.granted === true;
}

async function logAudit(userId, action, entityId, metadata) {
  const db = require('./config/database');
  await db.query(
    `INSERT INTO audit_logs (user_id, action, entity_type, entity_id, metadata)
     VALUES ($1, $2, $3, $4, $5)`,
    [userId, action, 'device', entityId, JSON.stringify(metadata)]
  );
}

// ============================================
// START SERVER
// ============================================
const PORT = process.env.PORT || 3000;
server.listen(PORT, () => {
  console.log(`✅ SmartControl Pro Backend running on port ${PORT}`);
  console.log(`   Environment: ${process.env.NODE_ENV}`);
  console.log(`   Health: http://localhost:${PORT}/health`);
});

module.exports = { app, server, io };