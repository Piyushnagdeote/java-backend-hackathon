CREATE TABLE blacklisted_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token TEXT NOT NULL,
    expiry DATETIME NOT NULL,

    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    created_by VARCHAR(255),
    deleted BOOLEAN DEFAULT FALSE,
    version BIGINT DEFAULT 0
);
