-- ✅ Insert roles safely
INSERT INTO roles (name)
SELECT 'ROLE_ADMIN'
WHERE NOT EXISTS (
    SELECT 1 FROM roles WHERE name = 'ROLE_ADMIN'
);

INSERT INTO roles (name)
SELECT 'ROLE_USER'
WHERE NOT EXISTS (
    SELECT 1 FROM roles WHERE name = 'ROLE_USER'
);

-- ✅ Insert admin user safely
INSERT INTO users (username, password, enabled)
SELECT
    'admin',
    '$2a$10$7wZQn4Z8zZ2b3s7zYV7k0e9qzFj9JQFZk1O0qJv3fP1z8Q4E9O8m6',
    true
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'admin'
);

-- ✅ Assign ROLE_ADMIN to admin user (NO hard-coded IDs)
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.name = 'ROLE_ADMIN'
WHERE u.username = 'admin'
AND NOT EXISTS (
    SELECT 1
    FROM user_roles ur
    WHERE ur.user_id = u.id
      AND ur.role_id = r.id
);
