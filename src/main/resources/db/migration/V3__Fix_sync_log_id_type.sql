-- Fix sync_log table ID column type from SERIAL to BIGSERIAL

-- Drop the existing sync_log table if it exists
DROP TABLE IF EXISTS sync_log;

-- Recreate sync_log table with correct BIGSERIAL type for id
CREATE TABLE sync_log (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    sync_type VARCHAR(50) NOT NULL,
    sync_start TIMESTAMP NOT NULL,
    sync_end TIMESTAMP,
    items_uploaded INTEGER DEFAULT 0,
    items_downloaded INTEGER DEFAULT 0,
    conflicts_resolved INTEGER DEFAULT 0,
    status VARCHAR(50) NOT NULL DEFAULT 'IN_PROGRESS',
    error_message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);