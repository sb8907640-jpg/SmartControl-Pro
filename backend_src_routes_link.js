const express = require('express');
const { v4: uuidv4 } = require('uuid');
const auth = require('../middleware/auth');
const db = require('../config/database');
const router = express.Router();

// Generate invite link
router.post('/generate', auth, async (req, res) => {
  const { deviceType, permissions, expiryHours = 24 } = req.body;

  try {
    // Generate unique short code
    const code = Math.random().toString(36).substring(2, 8).toUpperCase();
    const expiresAt = new Date(Date.now() + expiryHours * 60 * 60 * 1000);

    const result = await db.query(
      `INSERT INTO invite_links 
       (code, owner_id, device_type, permissions, expires_at, status)
       VALUES ($1, $2, $3, $4, $5, 'active')
       RETURNING *`,
      [code, req.user.id, deviceType, JSON.stringify(permissions), expiresAt]
    );

    const link = result.rows[0];
    const inviteUrl = `${process.env.APP_URL}/invite/${code}`;

    res.json({
      id: link.id,
      code: link.code,
      url: inviteUrl,
      shortCode: `${code.substring(0, 3)}-${code.substring(3, 6)}`,
      expiresAt: link.expires_at,
      deviceType: link.device_type,
      permissions: link.permissions
    });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// Verify invite code
router.get('/verify/:code', async (req, res) => {
  try {
    const result = await db.query(
      `SELECT il.*, u.name as owner_name, u.email as owner_email
       FROM invite_links il
       JOIN users u ON u.id = il.owner_id
       WHERE il.code = $1 
         AND il.status = 'active'
         AND il.expires_at > NOW()`,
      [req.params.code.toUpperCase()]
    );

    if (result.rows.length === 0) {
      return res.status(404).json({ 
        error: 'Invalid or expired invite code' 
      });
    }

    const link = result.rows[0];

    res.json({
      valid: true,
      ownerName: link.owner_name,
      ownerEmail: link.owner_email,
      deviceType: link.device_type,
      permissions: link.permissions,
      expiresAt: link.expires_at
    });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// Use invite (link device)
router.post('/use/:code', async (req, res) => {
  const { deviceName, deviceType, osVersion, fcmToken, consents } = req.body;

  try {
    const client = await db.getClient();
    await client.query('BEGIN');

    // Verify invite
    const inviteResult = await client.query(
      `SELECT * FROM invite_links 
       WHERE code = $1 AND status = 'active' AND expires_at > NOW()
       FOR UPDATE`,
      [req.params.code.toUpperCase()]
    );

    if (inviteResult.rows.length === 0) {
      await client.query('ROLLBACK');
      return res.status(404).json({ error: 'Invalid or expired invite' });
    }

    const invite = inviteResult.rows[0];

    // Create device
    const deviceResult = await client.query(
      `INSERT INTO devices 
       (user_id, device_name, device_type, os_version, app_variant, status, fcm_token, linked_at, last_seen_at)
       VALUES ($1, $2, $3, $4, 'receiver_lite', 'linked', $5, NOW(), NOW())
       RETURNING *`,
      [invite.owner_id, deviceName, deviceType, osVersion, fcmToken]
    );

    const device = deviceResult.rows[0];

    // Record consents (only the ones explicitly granted)
    if (consents && Array.isArray(consents)) {
      for (const feature of consents) {
        await client.query(
          `INSERT INTO consents 
           (device_id, feature_name, granted, granted_at, ip_address)
           VALUES ($1, $2, true, NOW(), $3)`,
          [device.id, feature, req.ip]
        );
      }
    }

    // Mark invite as used
    await client.query(
      `UPDATE invite_links 
       SET status = 'used', used_at = NOW(), used_by_device = $1
       WHERE id = $2`,
      [device.id, invite.id]
    );

    // Audit log
    await client.query(
      `INSERT INTO audit_logs (user_id, device_id, action, metadata, ip_address)
       VALUES ($1, $2, $3, $4, $5)`,
      [
        invite.owner_id,
        device.id,
        'DEVICE_LINKED',
        JSON.stringify({ deviceName, deviceType, consents }),
        req.ip
      ]
    );

    await client.query('COMMIT');

    // Notify owner
    const io = req.app.get('io');
    if (io) {
      io.to(`user:${invite.owner_id}`).emit('device:linked', {
        deviceId: device.id,
        deviceName: device.device_name,
        linkedAt: device.linked_at
      });
    }

    res.status(201).json({
      success: true,
      device,
      message: 'Device linked successfully'
    });
  } catch (err) {
    await client.query('ROLLBACK');
    console.error('Link error:', err);
    res.status(500).json({ error: err.message });
  } finally {
    client.release();
  }
});

// List all invites (owner only)
router.get('/', auth, async (req, res) => {
  try {
    const result = await db.query(
      `SELECT * FROM invite_links 
       WHERE owner_id = $1 
       ORDER BY created_at DESC`,
      [req.user.id]
    );

    res.json(result.rows);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// Cancel invite
router.delete('/:id', auth, async (req, res) => {
  try {
    await db.query(
      `UPDATE invite_links 
       SET status = 'cancelled' 
       WHERE id = $1 AND owner_id = $2`,
      [req.params.id, req.user.id]
    );

    res.json({ success: true });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

module.exports = router;