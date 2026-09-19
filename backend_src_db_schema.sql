-- ============================================
-- SMARTCONTROL PRO — DATABASE SCHEMA
-- ============================================

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- ============================================
-- USERS
-- ============================================
CREATE TABLE users (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  email VARCHAR(255) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  name VARCHAR(255),
  mobile VARCHAR(15),
  role VARCHAR(50) DEFAULT 'user' CHECK (role IN ('user', 'owner', 'admin', 'super_admin')),
  email_verified BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP DEFAULT NOW(),
  updated_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_users_email ON users(email);

-- ============================================
-- DEVICES (Only user's own devices)
-- ============================================
CREATE TABLE devices (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID REFERENCES users(id) ON DELETE CASCADE,
  device_name VARCHAR(255),
  device_type VARCHAR(50) CHECK (device_type IN ('android', 'ios', 'laptop', 'desktop', 'tablet', 'web')),
  os_version VARCHAR(100),
  app_variant VARCHAR(20) CHECK (app_variant IN ('owner', 'receiver_lite', 'receiver_full')),
  status VARCHAR(20) DEFAULT 'pending' CHECK (status IN ('pending', 'linked', 'unlinked', 'blocked')),
  fcm_token TEXT,
  linked_at TIMESTAMP,
  unlinked_at TIMESTAMP,
  last_seen_at TIMESTAMP,
  created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_devices_user ON devices(user_id);
CREATE INDEX idx_devices_status ON devices(status);

-- ============================================
-- CONSENTS (Explicit, revocable)
-- ============================================
CREATE TABLE consents (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  device_id UUID REFERENCES devices(id) ON DELETE CASCADE,
  user_id UUID REFERENCES users(id),
  feature_name VARCHAR(100) NOT NULL,
  granted BOOLEAN DEFAULT FALSE,
  granted_at TIMESTAMP,
  revoked_at TIMESTAMP,
  ip_address INET,
  device_fingerprint TEXT,
  signature TEXT,
  metadata JSONB,
  created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_consents_device ON consents(device_id);
CREATE INDEX idx_consents_feature ON consents(feature_name);
CREATE INDEX idx_consents_granted ON consents(granted);

-- ============================================
-- INVITE LINKS
-- ============================================
CREATE TABLE invite_links (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  code VARCHAR(12) UNIQUE NOT NULL,
  owner_id UUID REFERENCES users(id) ON DELETE CASCADE,
  device_type VARCHAR(50),
  permissions JSONB NOT NULL,
  expires_at TIMESTAMP NOT NULL,
  used_at TIMESTAMP,
  used_by_device UUID REFERENCES devices(id),
  status VARCHAR(20) DEFAULT 'active' CHECK (status IN ('active', 'used', 'expired', 'cancelled')),
  created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_invite_code ON invite_links(code);
CREATE INDEX idx_invite_status ON invite_links(status);

-- ============================================
-- AUDIT LOGS (Everything tracked)
-- ============================================
CREATE TABLE audit_logs (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID REFERENCES users(id),
  device_id UUID REFERENCES devices(id),
  action VARCHAR(100) NOT NULL,
  entity_type VARCHAR(50),
  entity_id UUID,
  metadata JSONB,
  ip_address INET,
  user_agent TEXT,
  created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_audit_user ON audit_logs(user_id);
CREATE INDEX idx_audit_device ON audit_logs(device_id);
CREATE INDEX idx_audit_action ON audit_logs(action);
CREATE INDEX idx_audit_created ON audit_logs(created_at DESC);

-- ============================================
-- LOCATION LOGS (Consent-based)
-- ============================================
CREATE TABLE location_logs (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  device_id UUID REFERENCES devices(id) ON DELETE CASCADE,
  latitude DECIMAL(10, 8) NOT NULL,
  longitude DECIMAL(11, 8) NOT NULL,
  accuracy DECIMAL(10, 2),
  altitude DECIMAL(10, 2),
  speed DECIMAL(10, 2),
  battery_level INT,
  is_charging BOOLEAN,
  network_type VARCHAR(30),
  timestamp TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_location_device ON location_logs(device_id);
CREATE INDEX idx_location_timestamp ON location_logs(timestamp DESC);

-- ============================================
-- SESSIONS (For touch, keyboard, screen, etc.)
-- ============================================
CREATE TABLE sessions (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  device_id UUID REFERENCES devices(id) ON DELETE CASCADE,
  owner_id UUID REFERENCES users(id),
  session_type VARCHAR(50) NOT NULL,
  started_at TIMESTAMP DEFAULT NOW(),
  ended_at TIMESTAMP,
  stop_pressed BOOLEAN DEFAULT FALSE,
  stopped_by UUID REFERENCES users(id),
  metadata JSONB,
  audit_log JSONB
);

CREATE INDEX idx_sessions_device ON sessions(device_id);
CREATE INDEX idx_sessions_type ON sessions(session_type);

-- ============================================
-- OFFLINE RECORDINGS
-- ============================================
CREATE TABLE offline_recordings (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  device_id UUID REFERENCES devices(id) ON DELETE CASCADE,
  owner_id UUID REFERENCES users(id),
  recording_type VARCHAR(30) CHECK (recording_type IN ('video', 'audio', 'photo', 'screen')),
  file_name VARCHAR(300),
  file_size BIGINT,
  duration INT,
  format VARCHAR(20),
  quality VARCHAR(20),
  recorded_at TIMESTAMP,
  sync_status VARCHAR(20) DEFAULT 'pending' CHECK (sync_status IN ('pending', 'syncing', 'synced', 'failed')),
  synced_at TIMESTAMP,
  storage_path TEXT,
  audit_log JSONB,
  created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_offline_device ON offline_recordings(device_id);
CREATE INDEX idx_offline_sync ON offline_recordings(sync_status);

-- ============================================
-- DATA ACCESS LOGS
-- ============================================
CREATE TABLE data_access_logs (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  device_id UUID REFERENCES devices(id),
  user_id UUID REFERENCES users(id),
  data_type VARCHAR(50),
  action VARCHAR(30),
  file_path TEXT,
  ip_address INET,
  accessed_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_data_access_device ON data_access_logs(device_id);

-- ============================================
-- NOTIFICATIONS
-- ============================================
CREATE TABLE notifications (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID REFERENCES users(id) ON DELETE CASCADE,
  device_id UUID REFERENCES devices(id),
  title VARCHAR(255),
  body TEXT,
  type VARCHAR(50),
  data JSONB,
  read BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_notifications_user ON notifications(user_id);
CREATE INDEX idx_notifications_read ON notifications(read);