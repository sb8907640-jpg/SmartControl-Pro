const express = require('express');
const auth = require('../middleware/auth');
const consentCheck = require('../middleware/consentCheck');
const db = require('../config/database');
const router = express.Router();

// Get location history (requires consent)
router.get('/:deviceId', 
  auth, 
  consentCheck('location'), 
  async (req, res) => {
    try {
      const { limit = 100, from, to } = req.query;
      
      let query = `SELECT * FROM location_logs WHERE device_id = $1`;
      const params = [req.params.deviceId];
      
      if (from) {
        params.push(from);
        query += ` AND timestamp >= $${params.length}`;
      }
      if (to) {
        params.push(to);
        query += ` AND timestamp <= $${params.length}`;
      }
      
      params.push(parseInt(limit));
      query += ` ORDER BY timestamp DESC LIMIT $${params.length}`;

      const result = await db.query(query, params);

      // Log access
      await db.query(
        `INSERT INTO data_access_logs (device_id, user_id, data_type, action, ip_address)
         VALUES ($1, $2, $3, $4, $5)`,
        [req.params.deviceId, req.user.id, 'location', 'view', req.ip]
      );

      res.json(result.rows);
    } catch (err) {
      res.status(500).json({ error: err.message });
    }
  }
);

// Post location (device sends)
router.post('/:deviceId', 
  auth, 
  consentCheck('location'), 
  async (req, res) => {
    const { latitude, longitude, accuracy, altitude, speed, batteryLevel, isCharging, networkType } = req.body;

    try {
      const result = await db.query(
        `INSERT INTO location_logs 
         (device_id, latitude, longitude, accuracy, altitude, speed, battery_level, is_charging, network_type)
         VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
         RETURNING *`,
        [req.params.deviceId, latitude, longitude, accuracy, altitude, speed, batteryLevel, isCharging, networkType]
      );

      // Notify owner via socket
      const io = req.app.get('io');
      if (io) {
        const device = await db.query('SELECT user_id FROM devices WHERE id = $1', [req.params.deviceId]);
        if (device.rows.length > 0) {
          io.to(`user:${device.rows[0].user_id}`).emit('location:update', {
            deviceId: req.params.deviceId,
            location: result.rows[0]
          });
        }
      }

      res.json(result.rows[0]);
    } catch (err) {
      res.status(500).json({ error: err.message });
    }
  }
);

module.exports = router;