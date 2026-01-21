INSERT INTO users (username, password, enabled)
SELECT
    'admin',
    '$2a$10$7wZQn4Z8zZ2b3s7zYV7k0e9qzFj9JQFZk1O0qJv3fP1z8Q4E9O8m6',
    true
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username='admin'
);
