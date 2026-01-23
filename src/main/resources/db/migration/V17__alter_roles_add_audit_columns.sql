-- Fix roles table to match BaseEntity

ALTER TABLE roles
    ADD COLUMN created_at DATETIME NULL,
    ADD COLUMN updated_at DATETIME NULL,
    ADD COLUMN created_by VARCHAR(255) NULL,
    ADD COLUMN deleted BIT(1) NOT NULL DEFAULT 0,
    ADD COLUMN version BIGINT DEFAULT 0;
