const express = require('express');
const auth = require('../middleware/auth');
const db = require('../config/database');
const router = express.Router();

// Get all consents for a device
router.get('/:deviceId', auth, async (req, res) => {
  try {
    const device = await db.query(
      'SELECT * FROM devices WHERE id = $1 AND user_id = $2',
      [req.params.deviceId, req.user.id]
    );

    if (device.rows.length === 0) {
      return res.status(404).json({ error: 'Device not found' });
    }

    const consents = await db.query(
      `SELECT DISTINCT ON (feature_name) *
       FROM consents
       WHERE device_id = $1
       ORDER BY feature_name, granted_at DESC`,
      [req.params.deviceId]
    );

    res.json(consents.rows);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// Grant consent (Receiver allows feature)
router.post('/grant', auth, async (req, res) => {
  const { deviceId, featureName, metadata } = req.body;

  try {
    // Verify device
    const device = await db.query(
      'SELECT * FROM devices WHERE id = $1',
      [deviceId]
    );

    if (device.rows.length === 0) {
      return res.status(404).json({ error: 'Device not found' });
    }

    // Record consent
    const consent = await db.query(
      `INSERT INTO consents 
       (device_id, user_id, feature_name, granted, granted_at, ip_address, metadata)
       VALUES ($1, $2, $3, true, NOW(), $4, $5)
       RETURNING *`,
      [deviceId, req.user.id, featureName, req.ip, metadata || {}]
    );

    // Audit log
    await db.query(
      `INSERT INTO audit_logs (user_id, device_id, action, entity_type, entity_id, metadata, ip_address)
       VALUES ($1, $2, $3, $4, $5, $6, $7)`,
      [
        req.user.id,
        deviceId,
        'CONSENT_GRANTED',
        'consent',
        consent.rows[0].id,
        JSON.stringify({ featureName }),
        req.ip
      ]
    );

    // Notify owner via Socket.IO
    const io = req.app.get('io');
    if (io) {
      io.to(`user:${device.rows[0].user_id}`).emit('consent:granted', {
        deviceId,
        featureName,
        grantedAt: consent.rows[0].granted_at
      });
    }

    res.json(consent.rows[0]);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// Revoke consent (Receiver can always revoke)
router.post('/revoke', auth, async (req, res) => {
  const { deviceId, featureName } = req.body;

  try {
    const result = await db.query(
      `UPDATE consents 
       SET granted = false, revoked_at = NOW()
       WHERE device_id = $1 
         AND feature_name = $2 
         AND granted = true
         AND revoked_at IS NULL
       RETURNING *`,
      [deviceId, featureName]
    );

    if (result.rows.length === 0) {
      return res.status(404).json({ error: 'No active consent found' });
    }

    // Audit log
    await db.query(
      `INSERT INTO audit_logs (user_id, device_id, action, entity_type, entity_id, metadata, ip_address)
       VALUES ($1, $2, $3, $4, $5, $6, $7)`,
      [
        req.user.id,
        deviceId,
        'CONSENT_REVOKED',
        'consent',
        result.rows[0].id,
        JSON.stringify({ featureName }),
        req.ip
      ]
    );

    res.json(result.rows[0]);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// Revoke ALL consents (Panic button)
router.post('/revoke-all', auth, async (req, res) => {
  const { deviceId } = req.body;

  try {
    await db.query(
      `UPDATE consents 
       SET granted = false, revoked_at = NOW()
       WHERE device_id = $1 AND granted = true AND revoked_at IS NULL`,
      [deviceId]
    );

    // Audit log
    await db.query(
      `INSERT INTO audit_logs (user_id, device_id, action, metadata, ip_address)
       VALUES ($1, $2, $3, $4, $5)`,
      [
        req.user.id,
        deviceId,
        'CONSENT_REVOKED_ALL',
        JSON.stringify({ revokedAt: new Date() }),
        req.ip
      ]
    );

    res.json({ success: true, message: 'All consents revoked' });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

module.exports = router;