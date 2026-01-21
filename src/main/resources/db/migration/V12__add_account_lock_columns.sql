-- failed_attempts
SET @sql := (
    SELECT IF(
        COUNT(*) = 0,
        'ALTER TABLE users ADD COLUMN failed_attempts INT DEFAULT 0',
        'SELECT 1'
    )
    FROM information_schema.columns
    WHERE table_schema = DATABASE()
      AND table_name = 'users'
      AND column_name = 'failed_attempts'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- lock_time
SET @sql := (
    SELECT IF(
        COUNT(*) = 0,
        'ALTER TABLE users ADD COLUMN lock_time DATETIME NULL',
        'SELECT 1'
    )
    FROM information_schema.columns
    WHERE table_schema = DATABASE()
      AND table_name = 'users'
      AND column_name = 'lock_time'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
