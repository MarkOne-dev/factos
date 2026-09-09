-- Flyway Migration V3: Migration for SHA-256 Hashed API Keys
-- Adds documentation comment indicating key_value column contains 64-char SHA-256 hex digest

COMMENT ON COLUMN api_keys.key_value IS 'SHA-256 hex digest of the API Key';
