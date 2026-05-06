-- Create test ADMIN user for debugging
-- Run this script to create a test admin user if it doesn't exist

-- First, create auth account
INSERT IGNORE INTO auth_accounts (id, email, password_hash, status, created_at, updated_at)
VALUES (
    999, 
    'admin@test.com', 
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', -- password: admin123
    'ACTIVE', 
    NOW(), 
    NOW()
);

-- Then create user with ADMIN role
INSERT IGNORE INTO users (id, auth_id, full_name, email, phone_number, role, verified, deleted, created_at, updated_at)
VALUES (
    999,
    999,
    'Test Admin',
    'admin@test.com',
    '0123456789',
    'ADMIN',
    true,
    false,
    NOW(),
    NOW()
);

-- Check if admin user exists
SELECT * FROM users WHERE role = 'ADMIN';

-- Check auth account
SELECT * FROM auth_accounts WHERE email = 'admin@test.com';
