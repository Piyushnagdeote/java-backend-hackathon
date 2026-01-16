-- ===============================
-- blacklisted_tokens
-- ===============================
SET @sql := (
    SELECT IF(COUNT(*)=0,
    'ALTER TABLE blacklisted_tokens ADD COLUMN created_at DATETIME NULL',
    'SELECT 1')
    FROM information_schema.columns
    WHERE table_schema = DATABASE()
    AND table_name='blacklisted_tokens'
    AND column_name='created_at'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql := (
    SELECT IF(COUNT(*)=0,
    'ALTER TABLE blacklisted_tokens ADD COLUMN updated_at DATETIME NULL',
    'SELECT 1')
    FROM information_schema.columns
    WHERE table_schema = DATABASE()
    AND table_name='blacklisted_tokens'
    AND column_name='updated_at'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql := (
    SELECT IF(COUNT(*)=0,
    'ALTER TABLE blacklisted_tokens ADD COLUMN created_by VARCHAR(255) NULL',
    'SELECT 1')
    FROM information_schema.columns
    WHERE table_schema = DATABASE()
    AND table_name='blacklisted_tokens'
    AND column_name='created_by'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql := (
    SELECT IF(COUNT(*)=0,
    'ALTER TABLE blacklisted_tokens ADD COLUMN deleted BOOLEAN DEFAULT FALSE',
    'SELECT 1')
    FROM information_schema.columns
    WHERE table_schema = DATABASE()
    AND table_name='blacklisted_tokens'
    AND column_name='deleted'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql := (
    SELECT IF(COUNT(*)=0,
    'ALTER TABLE blacklisted_tokens ADD COLUMN version BIGINT DEFAULT 0',
    'SELECT 1')
    FROM information_schema.columns
    WHERE table_schema = DATABASE()
    AND table_name='blacklisted_tokens'
    AND column_name='version'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
