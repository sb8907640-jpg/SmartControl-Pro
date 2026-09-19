const db = require('../config/database');

/**
 * CRITICAL: Every sensitive feature MUST check consent
 * If consent not granted → 403 Forbidden
 */
module.exports = (featureName) => {
  return async (req, res, next) => {
    try {
      const deviceId = req.params.deviceId || req.body.deviceId;
      
      if (!deviceId) {
        return res.status(400).json({ error: 'Device ID required' });
      }

      // Check consent
      const consent = await db.query(
        `SELECT * FROM consents 
         WHERE device_id = $1 
           AND feature_name = $2 
           AND granted = true
           AND revoked_at IS NULL
         ORDER BY granted_at DESC 
         LIMIT 1`,
        [deviceId, featureName]
      );

      if (consent.rows.length === 0) {
        // Log denied attempt
        await db.query(
          `INSERT INTO audit_logs (user_id, action, entity_type, entity_id, metadata, ip_address)
           VALUES ($1, $2, $3, $4, $5, $6)`,
          [
            req.user.id,
            'CONSENT_DENIED',
            'device',
            deviceId,
            JSON.stringify({ feature: featureName }),
            req.ip
          ]
        );

        return res.status(403).json({
          error: 'Consent not granted for this feature',
          feature: featureName,
          message: 'User must explicitly allow this feature'
        });
      }

      // Check device is linked
      const device = await db.query(
        `SELECT * FROM devices 
         WHERE id = $1 AND status = 'linked'`,
        [deviceId]
      );

      if (device.rows.length === 0) {
        return res.status(403).json({ error: 'Device not linked' });
      }

      req.consent = consent.rows[0];
      req.device = device.rows[0];
      next();
    } catch (err) {
      console.error('Consent check error:', err);
      res.status(500).json({ error: 'Internal server error' });
    }
  };
};