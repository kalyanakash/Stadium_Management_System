DROP TABLE IF EXISTS ticket;

DROP TABLE IF EXISTS event;

DROP TABLE IF EXISTS customer;

DROP TABLE IF EXISTS stadium;

-- Create Stadium Table
CREATE TABLE stadium (
    stadium_id INT AUTO_INCREMENT PRIMARY KEY,
    stadium_name VARCHAR(100) NOT NULL UNIQUE,
    location VARCHAR(100),
    total_seats INT
);

-- Create Event Table
CREATE TABLE event (
    event_id INT AUTO_INCREMENT PRIMARY KEY,
    event_name VARCHAR(100) NOT NULL,
    event_date DATE,
    stadium_name VARCHAR(100),
    FOREIGN KEY (stadium_name) REFERENCES stadium (stadium_name)
);

-- Create Customer Table
CREATE TABLE customer (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20)
);

-- Create Ticket Table
CREATE TABLE ticket (
    ticket_id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT,
    customer_id INT,
    seat_no VARCHAR(10),
    price DECIMAL(10, 2),
    FOREIGN KEY (event_id) REFERENCES event (event_id),
    FOREIGN KEY (customer_id) REFERENCES customer (customer_id)
);