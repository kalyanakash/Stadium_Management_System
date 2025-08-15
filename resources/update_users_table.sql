-- Add email and phone columns to users table
ALTER TABLE users ADD COLUMN email VARCHAR(100);

ALTER TABLE users ADD COLUMN phone VARCHAR(20);