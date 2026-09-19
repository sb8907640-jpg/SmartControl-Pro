const express = require('express');
const auth = require('../middleware/auth');
const db = require('../config/database');
const router = express.Router();

// List user's devices
router.get('/', auth, async (req, res) => {
  try {
    const result = await db.query(
      `SELECT d.*, 
              (SELECT COUNT(*) FROM consents c WHERE c.device_id = d.id AND c.granted = true AND c.revoked_at IS NULL) as active_consents,
              (SELECT json_agg(DISTINCT c.feature_name) FROM consents c WHERE c.device_id = d.id AND c.granted = true AND c.revoked_at IS NULL) as features
       FROM devices d
       WHERE d.user_id = $1
       ORDER BY d.created_at DESC`,
      [req.user.id]
    );

    res.json(result.rows);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// Get device detail
router.get('/:deviceId', auth, async (req, res) => {
  try {
    const result = await db.query(
      `SELECT d.*,
              (SELECT json_agg(c.*) FROM consents c WHERE c.device_id = d.id) as consents
       FROM devices d
       WHERE d.id = $1 AND d.user_id = $2`,
      [req.params.deviceId, req.user.id]
    );

    if (result.rows.length === 0) {
      return res.status(404).json({ error: 'Device not found' });
    }

    res.json(result.rows[0]);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// Unlink device
router.delete('/:deviceId', auth, async (req, res) => {
  const { reason } = req.body;

  try {
    const result = await db.query(
      `UPDATE devices 
       SET status = 'unlinked', unlinked_at = NOW()
       WHERE id = $1 AND user_id = $2
       RETURNING *`,
      [req.params.deviceId, req.user.id]
    );

    if (result.rows.length === 0) {
      return res.status(404).json({ error: 'Device not found' });
    }

    // Revoke all consents
    await db.query(
      `UPDATE consents 
       SET granted = false, revoked_at = NOW()
       WHERE device_id = $1 AND granted = true`,
      [req.params.deviceId]
    );

    // Audit log
    await db.query(
      `INSERT INTO audit_logs (user_id, device_id, action, metadata, ip_address)
       VALUES ($1, $2, $3, $4, $5)`,
      [
        req.user.id,
        req.params.deviceId,
        'DEVICE_UNLINKED',
        JSON.stringify({ reason }),
        req.ip
      ]
    );

    // Notify via socket
    const io = req.app.get('io');
    if (io) {
      io.to(`device:${req.params.deviceId}`).emit('device:unlinked', {
        reason,
        unlinkedAt: new Date().toISOString()
      });
    }

    res.json({ success: true, device: result.rows[0] });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

module.exports = router;