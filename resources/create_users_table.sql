-- Create Users Table for Login
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role ENUM('admin', 'user') NOT NULL
);

-- Add default admin and user accounts
INSERT INTO
    users (username, password, role)
VALUES ('admin', 'admin123', 'admin'),
    ('user1', 'user123', 'user'),
    ('user2', 'user456', 'user');