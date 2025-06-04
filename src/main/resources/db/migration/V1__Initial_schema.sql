-- Prayer Sync Database Schema
-- Initial migration that creates all tables

-- Churches table
CREATE TABLE IF NOT EXISTS churches (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    address VARCHAR(500),
    city VARCHAR(100),
    state VARCHAR(50),
    zip_code VARCHAR(20),
    country VARCHAR(50) DEFAULT 'USA',
    phone VARCHAR(20),
    email VARCHAR(255),
    website VARCHAR(255),
    pastor_name VARCHAR(255),
    denomination VARCHAR(100),
    service_times TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(255) PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100),
    phone VARCHAR(20),
    bio TEXT,
    profile_image_url VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE,
    is_church_admin BOOLEAN DEFAULT FALSE,
    church_id VARCHAR(255),
    reminder_enabled BOOLEAN DEFAULT FALSE,
    reminder_frequency_days INTEGER DEFAULT 7,
    reminder_time VARCHAR(10) DEFAULT '09:00',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (church_id) REFERENCES churches(id) ON DELETE SET NULL
);

-- Prayer Lists table
CREATE TABLE IF NOT EXISTS prayer_lists (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    privacy_level VARCHAR(20) NOT NULL DEFAULT 'PRIVATE',
    is_active BOOLEAN DEFAULT TRUE,
    creator_id VARCHAR(255) NOT NULL,
    church_id VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (creator_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (church_id) REFERENCES churches(id) ON DELETE SET NULL,
    CONSTRAINT prayer_lists_privacy_level_check CHECK (privacy_level IN ('PUBLIC', 'CHURCH', 'PRIVATE'))
);

-- Prayer Requests table
CREATE TABLE IF NOT EXISTS prayer_requests (
    id VARCHAR(255) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(50) NOT NULL DEFAULT 'OTHER',
    priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    privacy_level VARCHAR(20) NOT NULL DEFAULT 'PRIVATE',
    is_anonymous BOOLEAN DEFAULT FALSE,
    prayer_count INTEGER DEFAULT 0,
    answered_at TIMESTAMP,
    answered_description TEXT,
    creator_id VARCHAR(255) NOT NULL,
    prayer_list_id VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (creator_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (prayer_list_id) REFERENCES prayer_lists(id) ON DELETE SET NULL,
    CONSTRAINT prayer_requests_category_check CHECK (category IN ('HEALTH', 'FAMILY', 'WORK', 'SPIRITUAL_GROWTH', 'RELATIONSHIPS', 'FINANCES', 'MINISTRY', 'OTHER')),
    CONSTRAINT prayer_requests_priority_check CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH')),
    CONSTRAINT prayer_requests_status_check CHECK (status IN ('ACTIVE', 'ANSWERED', 'NO_LONGER_NEEDED', 'ONGOING')),
    CONSTRAINT prayer_requests_privacy_level_check CHECK (privacy_level IN ('PUBLIC', 'CHURCH', 'PRIVATE'))
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_church_id ON users(church_id);
CREATE INDEX IF NOT EXISTS idx_prayer_lists_creator_id ON prayer_lists(creator_id);
CREATE INDEX IF NOT EXISTS idx_prayer_lists_church_id ON prayer_lists(church_id);
CREATE INDEX IF NOT EXISTS idx_prayer_requests_creator_id ON prayer_requests(creator_id);
CREATE INDEX IF NOT EXISTS idx_prayer_requests_prayer_list_id ON prayer_requests(prayer_list_id);
CREATE INDEX IF NOT EXISTS idx_prayer_requests_status ON prayer_requests(status);
CREATE INDEX IF NOT EXISTS idx_prayer_requests_category ON prayer_requests(category);