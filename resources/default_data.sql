-- Default Indian Stadiums
INSERT INTO
    stadium (
        stadium_name,
        location,
        total_seats
    )
VALUES (
        'Eden Gardens',
        'Kolkata',
        66000
    ),
    (
        'Wankhede Stadium',
        'Mumbai',
        33000
    ),
    (
        'M. Chinnaswamy Stadium',
        'Bengaluru',
        40000
    ),
    (
        'Feroz Shah Kotla',
        'Delhi',
        41820
    ),
    (
        'Rajiv Gandhi Intl. Cricket Stadium',
        'Hyderabad',
        55000
    ),
    (
        'MA Chidambaram Stadium',
        'Chennai',
        50000
    ),
    (
        'Sardar Patel Stadium',
        'Ahmedabad',
        110000
    ),
    (
        'Holkar Cricket Stadium',
        'Indore',
        30000
    ),
    (
        'Green Park Stadium',
        'Kanpur',
        32000
    ),
    (
        'Barabati Stadium',
        'Cuttack',
        45000
    );

-- Default Events
INSERT INTO
    event (
        event_name,
        event_date,
        stadium_name
    )
VALUES (
        'IPL Final',
        '2025-05-29',
        'Eden Gardens'
    ),
    (
        'Ranji Trophy',
        '2025-01-15',
        'Wankhede Stadium'
    ),
    (
        'International T20',
        '2025-03-10',
        'M. Chinnaswamy Stadium'
    ),
    (
        'Test Match',
        '2025-02-20',
        'Feroz Shah Kotla'
    ),
    (
        'Charity Match',
        '2025-04-05',
        'Rajiv Gandhi Intl. Cricket Stadium'
    );

-- Default Customers
INSERT INTO
    customer (name, email, phone)
VALUES (
        'Rahul Sharma',
        'rahul.sharma@example.com',
        '9876543210'
    ),
    (
        'Priya Singh',
        'priya.singh@example.com',
        '9123456780'
    ),
    (
        'Amit Patel',
        'amit.patel@example.com',
        '9988776655'
    ),
    (
        'Sneha Reddy',
        'sneha.reddy@example.com',
        '9001122334'
    ),
    (
        'Vikram Chauhan',
        'vikram.chauhan@example.com',
        '9112233445'
    );

-- Default Tickets (random seats/prices)
INSERT INTO
    ticket (
        event_id,
        customer_id,
        seat_no,
        price
    )
VALUES (1, 1, 'A101', 1500.00),
    (2, 2, 'B202', 1200.00),
    (3, 3, 'C303', 1800.00),
    (4, 4, 'D404', 1000.00),
    (5, 5, 'E505', 2000.00);