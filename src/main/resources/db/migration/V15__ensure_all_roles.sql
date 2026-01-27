-- V15__ensure_all_roles.sql
INSERT IGNORE INTO roles (name) VALUES
('ROLE_ADMIN'),
('ROLE_EMPLOYEE'),
('ROLE_HR'),
('ROLE_MANAGER');
