-- Update all existing users to have default email and phone if missing
UPDATE users
SET
    email = CONCAT(username, '@example.com')
WHERE
    email IS NULL
    OR email = '';

UPDATE users
SET
    phone = '0000000000'
WHERE
    phone IS NULL
    OR phone = '';