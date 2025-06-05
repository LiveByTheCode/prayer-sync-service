-- Add sync metadata to track last sync times and handle conflicts

-- Add last_sync_at column to users table to track when each user last synced
ALTER TABLE users 
ADD COLUMN IF NOT EXISTS last_sync_at TIMESTAMP;

-- Add sync_id to prayer_requests for tracking sync state
ALTER TABLE prayer_requests
ADD COLUMN IF NOT EXISTS sync_id VARCHAR(255),
ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN DEFAULT FALSE,
ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;

-- Add sync_id to prayer_lists for tracking sync state  
ALTER TABLE prayer_lists
ADD COLUMN IF NOT EXISTS sync_id VARCHAR(255),
ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN DEFAULT FALSE,
ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;

-- Create indexes for sync operations
CREATE INDEX IF NOT EXISTS idx_prayer_requests_updated_at ON prayer_requests(updated_at);
CREATE INDEX IF NOT EXISTS idx_prayer_lists_updated_at ON prayer_lists(updated_at);
CREATE INDEX IF NOT EXISTS idx_prayer_requests_is_deleted ON prayer_requests(is_deleted);
CREATE INDEX IF NOT EXISTS idx_prayer_lists_is_deleted ON prayer_lists(is_deleted);

-- Create sync_log table to track sync operations
CREATE TABLE IF NOT EXISTS sync_log (
    id SERIAL PRIMARY KEY,
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